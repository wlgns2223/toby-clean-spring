package tobyspring.splearn.application.course.required;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import tobyspring.splearn.application.instructor.required.InstructorRepository;
import tobyspring.splearn.application.member.required.MemberRepository;
import tobyspring.splearn.domain.MemberFixture;
import tobyspring.splearn.domain.course.Course;
import tobyspring.splearn.domain.course.CourseFixture;
import tobyspring.splearn.domain.instructor.Instructor;
import tobyspring.splearn.domain.instructor.InstructorFixture;
import tobyspring.splearn.domain.member.Member;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CourseRepositoryTest  {

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    EntityManager entityManager;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    InstructorRepository instructorRepository;

    Member member;

    Instructor instructor;


    @BeforeEach
    void setUp(){
        member = memberRepository.save(MemberFixture.createActiveMember());
        instructor = instructorRepository.save(InstructorFixture.createActiveInstructor(member));
    }

    @Test
    @DisplayName("saveAndField")
    void saveAndField() {

        Course course = CourseFixture.createCourse(instructor,null);
        course = courseRepository.save(course);

        assertThat(course.getId()).isNotNull();

        entityManager.flush();
        entityManager.clear();

        Course found = courseRepository.findById(course.getId()).orElseThrow();
        assertThat(found).isEqualTo(course);
    }
    
    @Test
    @DisplayName("find by title containing")
    void findByTitleContaining() {
        List<Long> courseIds = Stream.of(CourseFixture.createCourse(instructor, "hello spring"),
                        CourseFixture.createCourse(instructor, "clean spring 2"),
                        CourseFixture.createCourse(instructor, "clean code"))
                .map(course -> courseRepository.save(course).getId()).toList();


        assertThat(courseRepository.findByTitleContaining("spring").stream().map(Course::getId))
                .isEqualTo(List.of(courseIds.get(0), courseIds.get(1)));

    }

    @Test
    @DisplayName("find by instructor")
    void findByInstructor() {
        Member member1 = memberRepository.save(MemberFixture.createActiveMember());
        Instructor instructor1 = instructorRepository.save(InstructorFixture.createActiveInstructor(member1));

        Course course = courseRepository.save(CourseFixture.createCourse(instructor, "title"));
        Course course2 = courseRepository.save(CourseFixture.createCourse(instructor1, "title2"));

        List<Course> courses = courseRepository.findByInstructorId(instructor.getId());

        assertThat(courses).singleElement().isEqualTo(course);

    }

}
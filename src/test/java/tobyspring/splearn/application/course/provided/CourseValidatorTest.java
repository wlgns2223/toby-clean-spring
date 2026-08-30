package tobyspring.splearn.application.course.provided;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import tobyspring.splearn.application.course.required.CourseRepository;
import tobyspring.splearn.domain.course.Course;
import tobyspring.splearn.domain.course.CourseFixture;
import tobyspring.splearn.domain.instructor.Instructor;
import tobyspring.splearn.support.exception.ValidationException;
import tobyspring.splearn.support.test.BaseApplicationService;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@SpringBootTest
class CourseValidatorTest extends BaseApplicationService {

    @Autowired
    CourseValidator courseValidator;

    @Autowired
    CourseRepository courseRepository;

    @Test
    @DisplayName("title duplication")
    void titleDuplication() {
        // given
        Instructor instructor = prepareInstructor();

        Instructor instructor2 = prepareInstructor();

        courseRepository.save(CourseFixture.createCourse(instructor, "clean spring"));
        courseRepository.save(CourseFixture.createCourse(instructor2, "clean code"));

        courseValidator.validateForCreate(instructor,new CourseCreateRequest(instructor.getId(), "spring 7",null));

        assertThatThrownBy(() ->
                courseValidator.validateForCreate(instructor, new CourseCreateRequest(instructor.getId(), "clean spring", null))
        ).isInstanceOfSatisfying(ValidationException.class,ve ->
                assertThat(ve.getErrors()).hasSize(1));

    }

    @Test
    @DisplayName("title update duplication")
    void titleUpdateDuplication() {
        Instructor instructor1 = prepareInstructor();
        Instructor instructor2 = prepareInstructor();

        Course course1 = courseRepository.save(CourseFixture.createCourse(instructor1, "clean spring"));
        Course course2 = courseRepository.save(CourseFixture.createCourse(instructor2, "clean code"));

        courseValidator.validateForUpdate(course1, CourseFixture.createCourseInfoUpdateRequest(course1.getTitle()));

        assertThatThrownBy(() ->
                courseValidator.validateForUpdate(course1, CourseFixture.createCourseInfoUpdateRequest("clean spring")))
                .isInstanceOfSatisfying(ValidationException.class, e -> assertThat(e.getErrors()).hasSize(1));


    }

}
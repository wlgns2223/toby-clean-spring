package tobyspring.splearn.application.curriculum.required;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import tobyspring.splearn.domain.cirriculumn.Curriculum;
import tobyspring.splearn.domain.course.Course;
import tobyspring.splearn.domain.course.CourseFixture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CurriculumRepositoryTest {

    @Autowired
    CurriculumRepository curriculumRepository;

    @Autowired
    EntityManager entityManager;
    
    @Test
    @DisplayName("saveAndFindById")
    void saveAndFindById() {
        // given
        Course course = CourseFixture.createCourse();
        Curriculum curriculum = new Curriculum(course);
        curriculum = curriculumRepository.save(curriculum);

        entityManager.flush();
        entityManager.clear();

        Curriculum found = curriculumRepository.findById(curriculum.getId()).orElseThrow();

        assertThat(found).isEqualTo(curriculum);


    }

}
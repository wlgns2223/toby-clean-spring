package tobyspring.splearn.domain.course;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tobyspring.splearn.domain.instructor.Instructor;
import tobyspring.splearn.domain.instructor.InstructorFixture;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CourseTest {

    Course course;

    @BeforeEach
    void setUp(){
        course = CourseFixture.createCourse();
    }
    
    @Test
    @DisplayName("create")
    void create() {
        // given
        Instructor instructor = InstructorFixture.createActiveInstructor();

        // when
        Course course = new Course(instructor, "clean spring 2", "description...");

        // then
        assertThat(course.getStatus()).isEqualTo(CourseStatus.DRAFT);
        assertThat(course.getInstructor()).isEqualTo(instructor);
        assertThat(course.getTitle()).isEqualTo("clean spring 2");
        assertThat(course.getDetail().getDescription()).isEqualTo("description...");
        assertThat(course.getDetail().getCreatedAt()).isNotNull();
    }

    @Test
    @DisplayName("create fail")
    void createFail() {
        // given
        Instructor instructor = InstructorFixture.createInstructor();

        assertThatThrownBy(() -> new Course(instructor, "title", null))
                .isInstanceOf(IllegalStateException.class);

    }

    @Test
    @DisplayName("submit for review")
    void submitForReview() {
        // given
        course.submitForReview();

        assertThat(course.getStatus()).isEqualTo(CourseStatus.IN_REVIEW);
    }

    @Test
    @DisplayName("submit for review fail")
    void submitForReviewFail() {
        // given
        Instructor instructor = InstructorFixture.createActiveInstructor();
        Course course = new Course(instructor, "clean spring 2", null);

        assertThatThrownBy(() -> course.submitForReview())
                .isInstanceOf(IllegalStateException.class);

    }

    @Test
    @DisplayName("update info")
    void updateInfo() {
        // given
        course.updateInfo(new CourseUpdateInfo("clean spring 3", "new desc"));

        assertThat(course.getTitle()).isEqualTo("clean spring 3");
        assertThat(course.getDetail().getDescription()).isEqualTo("new desc");

    }

}
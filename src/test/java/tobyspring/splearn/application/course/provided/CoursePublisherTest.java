package tobyspring.splearn.application.course.provided;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import tobyspring.splearn.domain.course.Course;
import tobyspring.splearn.domain.course.CourseStatus;
import tobyspring.splearn.support.test.BaseApplicationService;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class CoursePublisherTest extends BaseApplicationService {

    @Autowired
    CoursePublisher coursePublisher;

    @BeforeEach
    void setUp(){
        prepareInstructor();
        prepareCourse();

    }

    @Test
    @DisplayName("submit for review")
    void submitForReview() {
        // given
        Course publishedCourse = coursePublisher.submitForReview(course.getId());

        assertThat(publishedCourse.getStatus()).isEqualTo(CourseStatus.IN_REVIEW);

    }

    @Test
    @DisplayName("publish")
    void publish() {
        // given
        coursePublisher.submitForReview(course.getId());
        Course published = coursePublisher.publish(course.getId());

        assertThat(published.getStatus()).isEqualTo(CourseStatus.PUBLISHED);

    }

    @Test
    @DisplayName("archive")
    void archive() {
        coursePublisher.submitForReview(course.getId());
        coursePublisher.publish(course.getId());
        Course archived = coursePublisher.archive(course.getId());

        assertThat(archived.getStatus()).isEqualTo(CourseStatus.ARCHIVED);
    }

}
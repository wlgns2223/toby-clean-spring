package tobyspring.splearn.application.enrollment.provided;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import tobyspring.splearn.domain.course.Course;
import tobyspring.splearn.domain.enrollment.Enrollment;
import tobyspring.splearn.domain.enrollment.EnrollmentStatus;
import tobyspring.splearn.domain.member.Member;
import tobyspring.splearn.support.test.BaseApplicationService;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Transactional
@ActiveProfiles("test")
class EnrollerTest extends BaseApplicationService {

    @Autowired
    Enroller enroller;

    @Test
    void enroll() {
        Member activeMember = prepareActiveMember();
        Course publishedCourse = preparePublishedCourse();

        Enrollment enrollment = enroller.enroll(new EnrollRequest(activeMember.getId(), publishedCourse.getId()));

        assertThat(enrollment.getId()).isNotNull();

    }

    @Test
    void startStudying() {
        prepareEnrollment();

        Enrollment enrollmentStudying = enroller.startStudying(enrollment.getId());

        assertThat(enrollmentStudying.getStatus()).isEqualTo(EnrollmentStatus.STUDYING);

    }

    @Test
    void complete() {
        prepareEnrollment();

        Enrollment enrollerComplete = enroller.complete(enrollment.getId());

        assertThat(enrollerComplete.getStatus()).isEqualTo(EnrollmentStatus.COMPLETED);
    }
}
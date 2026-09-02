package tobyspring.splearn.domain.enrollment;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tobyspring.splearn.domain.MemberFixture;
import tobyspring.splearn.domain.course.Course;
import tobyspring.splearn.domain.course.CourseFixture;
import tobyspring.splearn.domain.member.Member;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class EnrollmentTest {

    @Test
    @DisplayName("enroll not published")
    void enrollNotPublished() {
        Member member = MemberFixture.createActiveMember();
        Course course = CourseFixture.createCourse();

        assertThatThrownBy(() -> Enrollment.enroll(member, course))
                .isInstanceOf(IllegalStateException.class);

    }

    @Test
    @DisplayName("enroll")
    void enroll() {
        Member member = MemberFixture.createActiveMember();
        Course course = CourseFixture.createPublishedCourse();

        Enrollment enrollment = Enrollment.enroll(member, course);

        assertThat(enrollment.getStatus()).isEqualTo(EnrollmentStatus.ENROLLED);

    }

    @Test
    @DisplayName("studying")
    void studying() {
        Enrollment enrollment = EnrollmentFixture.createEnrollment();
        enrollment.startStudying();
        assertThat(enrollment.getStatus()).isEqualTo(EnrollmentStatus.STUDYING);
        assertThatThrownBy(enrollment::startStudying).isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("complete")
    void complete() {
        Enrollment enrollment = EnrollmentFixture.createEnrollment();
        enrollment.startStudying();
        enrollment.complete();

        assertThat(enrollment.getStatus()).isEqualTo(EnrollmentStatus.COMPLETED);
        assertThat(enrollment.getCompleteAt()).isNotNull();
        assertThatThrownBy(enrollment::complete).isInstanceOf(IllegalStateException.class);
    }

}
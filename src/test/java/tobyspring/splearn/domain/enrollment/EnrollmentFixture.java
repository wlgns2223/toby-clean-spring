package tobyspring.splearn.domain.enrollment;

import jakarta.annotation.Nullable;
import tobyspring.splearn.domain.MemberFixture;
import tobyspring.splearn.domain.course.Course;
import tobyspring.splearn.domain.course.CourseFixture;
import tobyspring.splearn.domain.member.Member;

public class EnrollmentFixture {
    public static Enrollment createEnrollment(@Nullable Member member, @Nullable Course course){
        return Enrollment.enroll(
                member == null ? MemberFixture.createActiveMember() : member,
                course == null ? CourseFixture.createPublishedCourse() : course
        );
    }

    public static Enrollment createEnrollment(){
        return createEnrollment(null, null);
    }
}

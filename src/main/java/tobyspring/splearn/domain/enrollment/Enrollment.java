package tobyspring.splearn.domain.enrollment;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.NaturalId;
import org.springframework.util.Assert;
import tobyspring.splearn.domain.AbstractEntity;
import tobyspring.splearn.domain.course.Course;
import tobyspring.splearn.domain.member.Member;

import java.time.LocalDateTime;

@Entity
@Getter
@ToString(callSuper = true,exclude = {"member","course"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = @UniqueConstraint(name = "UK_ENROLLMENT_MEMBER_COURSE",columnNames = {"member_id", "course_id"}))
public class Enrollment extends AbstractEntity {

    @NaturalId
    @ManyToOne(optional = false,fetch = FetchType.LAZY)
    private Member member;

    @NaturalId
    @ManyToOne(optional = false,fetch = FetchType.LAZY)
    private Course course;

    @Enumerated(EnumType.STRING)
    private EnrollmentStatus status;

    private LocalDateTime enrolledAt;

    private LocalDateTime completeAt;

    public static Enrollment enroll(Member member, Course course){
        member.ensureActive();
        course.ensurePublished();

        Enrollment enrollment = new Enrollment();
        enrollment.member = member;
        enrollment.course = course;
        enrollment.status = EnrollmentStatus.ENROLLED;
        enrollment.enrolledAt = LocalDateTime.now();

        return enrollment;
    }

    public void startStudying() {
        Assert.state(status == EnrollmentStatus.ENROLLED,"수강상태가 ENROLLED가 아닙니다.");
        this.status = EnrollmentStatus.STUDYING;
    }

    public void complete() {
        Assert.state(status == EnrollmentStatus.STUDYING,"수강상태가 STUDYING이 아닙니다.");
        this.status = EnrollmentStatus.COMPLETED;
        this.completeAt = LocalDateTime.now();
    }
}

package tobyspring.splearn.domain.instructor;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.util.Assert;
import tobyspring.splearn.domain.AbstractEntity;
import tobyspring.splearn.domain.member.Member;

@Entity
@Getter
@ToString(callSuper = true,exclude = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Instructor extends AbstractEntity {

    @OneToOne(fetch = FetchType.LAZY)
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column
    private InstructorStatus status;

    public static Instructor apply(Member member) {
        Assert.state(member.isActive(), "등록 완료 상태가 아닌 회원은 강사 신청을 할 수 없습니다.");

        Instructor instructor = new Instructor();
        instructor.member = member;
        instructor.status = InstructorStatus.PENDING;

        return instructor;
    }

    public void approve() {
        Assert.state(status == InstructorStatus.PENDING, "강사의 상태가 PENDING이 아닙니다.");
        this.status = InstructorStatus.ACTIVE;
    }

    public void reject() {
        Assert.state(status == InstructorStatus.PENDING, "강사의 상태가 PENDING이 아닙니다.");
        this.status = InstructorStatus.REJECTED;
    }

    public boolean isActive() {
        return status == InstructorStatus.ACTIVE;
    }

    public void ensureActive() {
        Assert.state(isActive(),"active 상태가 아닙니다.");
    }
}

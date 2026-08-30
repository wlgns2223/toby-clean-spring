package tobyspring.splearn.domain.instructor;

import jakarta.validation.Valid;
import tobyspring.splearn.application.instructor.provided.InstructorApplyRequest;
import tobyspring.splearn.domain.MemberFixture;
import tobyspring.splearn.domain.member.Member;

public class InstructorFixture {

    public static Instructor createInstructor(Member member){
        return Instructor.apply(member);
    }

    public static Instructor createInstructor(){
        return createInstructor(MemberFixture.createActiveMember());
    }

    public static Instructor createActiveInstructor(){
        Instructor instructor = createInstructor();
        instructor.approve();
        return instructor;
    }

    public static Instructor createActiveInstructor(Member member) {
        Instructor instructor = createInstructor(member);
        instructor.approve();
        return instructor;
    }

    public static @Valid InstructorApplyRequest createApplyRequest(Member activeMember) {
        return new InstructorApplyRequest(activeMember.getId());
    }
}

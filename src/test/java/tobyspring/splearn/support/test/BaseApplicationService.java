package tobyspring.splearn.support.test;

import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import tobyspring.splearn.application.instructor.provided.InstructorApplication;
import tobyspring.splearn.application.member.provided.MemberRegister;
import tobyspring.splearn.domain.MemberFixture;
import tobyspring.splearn.domain.instructor.Instructor;
import tobyspring.splearn.domain.instructor.InstructorFixture;
import tobyspring.splearn.domain.member.Member;

@SpringBootTest
@Transactional
public class BaseApplicationService {
    @Autowired
    MemberRegister memberRegister;
    @Autowired
    InstructorApplication instructorApplication;

    protected Member member;
    protected Instructor instructor;

    @NonNull
    protected Instructor prepareInstructor() {
        this.member = prepareMember();
        this.instructor = instructorApplication.apply(InstructorFixture.createApplyRequest(member));

        instructor.approve();
        return this.instructor;
    }

    protected @NonNull Member prepareMember() {
        this.member = memberRegister.register(MemberFixture.createMemberRegisterRequest());
        this.member.activate();
        return this.member;
    }
}

package tobyspring.splearn.application.instructor.provided;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import tobyspring.splearn.application.member.provided.MemberRegister;
import tobyspring.splearn.domain.MemberFixture;
import tobyspring.splearn.domain.instructor.Instructor;
import tobyspring.splearn.domain.member.Member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class InstructorFinderTest {

    @Autowired
    InstructorFinder instructorFinder;

    @Autowired
    InstructorApplication instructorApplication;

    @Autowired
    MemberRegister memberRegister;
    
    @Test
    @DisplayName("findByMember")
    void findByMember() {
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());
        memberRegister.activate(member.getId());
        Instructor instructor = instructorApplication.apply(new InstructorApplyRequest(member.getId()));

        // when
        Instructor found = instructorFinder.findByMember(instructor.getId()).orElseThrow();

        // then
        assertThat(instructor).isEqualTo(found);

    }

}
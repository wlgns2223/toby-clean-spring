package tobyspring.splearn.application.instructor.provided;

import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;
import tobyspring.splearn.application.instructor.required.InstructorRepository;
import tobyspring.splearn.application.member.required.MemberRepository;
import tobyspring.splearn.domain.MemberFixture;
import tobyspring.splearn.domain.instructor.Instructor;
import tobyspring.splearn.domain.instructor.InstructorFixture;
import tobyspring.splearn.domain.instructor.InstructorStatus;
import tobyspring.splearn.domain.member.Member;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class InstructorApplicationTest {

    @Autowired
    InstructorApplication instructorApplication;

    @Autowired
    InstructorRepository instructorRepository;

    
    @Autowired
    MemberRepository memberRepository;
    
    @Test
    @DisplayName("apply")
    void appy() {
        // given
        Instructor instructor = preparePendingInstructor();

        // then
        assertThat(instructor.getId()).isNotNull();
        assertThat(instructor.getStatus()).isEqualTo(InstructorStatus.PENDING);

        Instructor foundInstructor = instructorRepository.findById(instructor.getId()).get();

    }

    @Test
    @DisplayName("approve")
    void approve() {
        Instructor instructor = preparePendingInstructor();

        Instructor approvedInstructor = instructorApplication.approve(instructor.getId());

        assertThat(approvedInstructor.getStatus()).isEqualTo(InstructorStatus.ACTIVE);

    }

    @Test
    @DisplayName("duplicate apply ")
    void duplicateApply() {
        Member activeMember = MemberFixture.createActiveMember();
        memberRepository.save(activeMember);

        instructorApplication.apply(InstructorFixture.createApplyRequest(activeMember));

        assertThatThrownBy(() -> instructorApplication.apply(InstructorFixture.createApplyRequest(activeMember)))
                .isInstanceOf(DuplicationInstructorApplicationException.class);

    }

    @Test
    @DisplayName("reject")
    void reject() {
        Instructor instructor = InstructorFixture.createInstructor();

        Instructor approvedInstructor = instructorApplication.reject(instructor.getId());

        assertThat(approvedInstructor.getStatus()).isEqualTo(InstructorStatus.REJECTED);

    }

    private Instructor preparePendingInstructor() {
        Member activeMember = MemberFixture.createActiveMember();
        memberRepository.save(activeMember);
        return instructorApplication.apply(InstructorFixture.createApplyRequest(activeMember));
    }

}
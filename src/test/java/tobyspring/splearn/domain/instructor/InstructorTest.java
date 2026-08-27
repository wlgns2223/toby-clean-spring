package tobyspring.splearn.domain.instructor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tobyspring.splearn.domain.MemberFixture;
import tobyspring.splearn.domain.member.Member;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class InstructorTest {

    @Test
    @DisplayName("apply")
    void apply() {
        // given
        Member member = MemberFixture.createActiveMember();

        // when
        Instructor instructor = Instructor.apply(member);

        // then
        assertThat(instructor.getMember()).isEqualTo(member);
        assertThat(instructor.getStatus()).isEqualTo(InstructorStatus.PENDING);
    }

    @Test
    @DisplayName("apply fail")
    void applyFail() {
        // given
        Member member = MemberFixture.createMember();

        // when

        // then
        assertThatThrownBy(() -> Instructor.apply(member)).isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("approve")
    void approve() {
        // given
        Member member = MemberFixture.createActiveMember();
        Instructor instructor = Instructor.apply(member);

        // when
        instructor.approve();

        // then
        assertThat(instructor.getStatus()).isEqualTo(InstructorStatus.ACTIVE);

    }

    @Test
    @DisplayName("approve fail")
    void approveFail() {
        Member member = MemberFixture.createActiveMember();
        Instructor instructor = Instructor.apply(member);

        // when
        instructor.approve();

        assertThatThrownBy(() -> instructor.approve()).isInstanceOf(IllegalStateException.class);

    }

    @Test
    @DisplayName("reject")
    void reject() {
        // given
        Member member = MemberFixture.createActiveMember();
        Instructor instructor = Instructor.apply(member);

        // when
        instructor.reject();

        // then
        assertThat(instructor.getStatus()).isEqualTo(InstructorStatus.REJECTED);

    }

    @Test
    @DisplayName("reject fail")
    void rejectFail() {
        Member member = MemberFixture.createActiveMember();
        Instructor instructor = Instructor.apply(member);

        // when
        instructor.reject();

        assertThatThrownBy(() -> instructor.reject()).isInstanceOf(IllegalStateException.class);

    }

    @Test
    @DisplayName("isActive")
    void isActive() {
        Member member = MemberFixture.createActiveMember();
        Instructor instructor = Instructor.apply(member);

        assertThat(instructor.isActive()).isFalse();

        instructor.approve();

        assertThat(instructor.isActive()).isTrue();

    }

    @Test
    @DisplayName("ensureActive")
    void ensureActive() {
        Member member = MemberFixture.createActiveMember();
        Instructor instructor = Instructor.apply(member);

        assertThatThrownBy(() -> instructor.ensureActive()).isInstanceOf(IllegalStateException.class);

    }

}
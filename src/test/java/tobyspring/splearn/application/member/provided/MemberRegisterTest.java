package tobyspring.splearn.application.member.provided;

import jakarta.persistence.EntityManager;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestConstructor;
import org.springframework.transaction.annotation.Transactional;
import tobyspring.splearn.SplearnTestConfiguration;
import tobyspring.splearn.domain.*;
import tobyspring.splearn.domain.member.*;

import static org.assertj.core.api.Assertions.*;

/**
 *
 * Transactional을 붙이면 테스트를 수행하는 동안에
 * 데이터베이스에 했던 모든 작업을 테스트가 끝날때 성공이든 실패든 상관없이 롤백을 시켜줌
 *
 * Classes annotated with '@Transactional' could be implicitly subclassed and must not be final
 * 위 에러는 무시. 버그임
 */

@SpringBootTest
@Transactional
@Import(SplearnTestConfiguration.class)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
public record MemberRegisterTest(MemberRegister memberRegister, EntityManager entityManager) {
    /**
     * Test작성시 class에 autowired가 너무 많으면 보기 힘드니,
     * JUnit에 따르면 간결하게 record로 작성해도 되지만, Bean 주입 방법을 명시해줘야함
     * @TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
     */

    @Test
    void register() {
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());

        assertThat(member.getId()).isNotNull();
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);

    }

    @Test
    void duplicateEmailFail() {
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());
        assertThatThrownBy(() -> memberRegister.register(MemberFixture.createMemberRegisterRequest()))
                .isInstanceOf(DuplicateEmailException.class);
    }

    @Test
    void memberRegisterRequestFail() {
        MemberRegisterRequest invalid = new MemberRegisterRequest("toby@splearn.app", "Toby", "verysecret");
        assertThatThrownBy(() -> memberRegister.register(invalid)).isInstanceOf(ConstraintViolationException.class);

    }

    @Test
    void activate() {
        Member member = getMember();

        member = memberRegister.activate(member.getId());

        entityManager.flush();

        assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVE);
    }

    @Test
    void deactivate() {
        Member member = getMember();

        memberRegister.activate(member.getId());
        entityManager.flush();
        entityManager.clear();

        member = memberRegister.deactivate(member.getId());

        assertThat(member.getStatus()).isEqualTo(MemberStatus.DEACTIVATED);
        assertThat(member.getDetail().getDeactivatedAt()).isNotNull();

    }

    private Member getMember() {
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());
        entityManager.flush();
        entityManager.clear();
        return member;
    }

    private Member getMember(String email) {
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest(email));
        entityManager.flush();
        entityManager.clear();
        return member;
    }

    @Test
    void updateInfo() {
        Member member = getMember();
        memberRegister.activate(member.getId());
        entityManager.flush();
        entityManager.clear();

        member = memberRegister.updateInfo(member.getId(), new MemberInfoUpdateRequest("leo1234", "toby", "introduction"));
        entityManager.flush();
        entityManager.clear();

        assertThat(member.getDetail().getProfile().value()).isEqualTo("toby");
    }

    @Test
    void updateInfoFail() {
        Member member = getMember();
        memberRegister.activate(member.getId());
        memberRegister.updateInfo(member.getId(), new MemberInfoUpdateRequest("peter", "toby100", "introduction"));

        Member member2 = getMember("anEmail@gmail.com");
        memberRegister.activate(member2.getId());

        entityManager.flush();
        entityManager.clear();


        assertThatThrownBy(() -> {
            memberRegister.updateInfo(
                    member2.getId(), new MemberInfoUpdateRequest("newNickname", "toby100", "introduction")
            );
        }).isInstanceOf(IllegalArgumentException.class);

    }
}

package tobyspring.splearn.application.provided;

import jakarta.persistence.EntityManager;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import tobyspring.splearn.SplearnTestConfiguration;
import tobyspring.splearn.domain.*;

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
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());

        /**
         * 영속성 컨텍스트를 비워줘야 실제 쿼리가 디비까지 이어지는지 확인할 수 있다.
         * flush가 없으면 insert 문만 로그에 남음
         */
        entityManager.flush();
        entityManager.clear();

        member = memberRegister.activate(member.getId());

        entityManager.flush();

        assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVE);
    }
}

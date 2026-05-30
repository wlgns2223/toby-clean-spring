package tobyspring.splearn.application.member.provided;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestConstructor;
import org.springframework.transaction.annotation.Transactional;
import tobyspring.splearn.SplearnTestConfiguration;
import tobyspring.splearn.domain.member.Member;
import tobyspring.splearn.domain.MemberFixture;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Import(SplearnTestConfiguration.class)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
record MemberFinderTest(MemberFinder memberFinder,
                        MemberRegister memberRegister,
                        EntityManager entityManager) {

    @Test
    void find() {
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());
        entityManager.flush();
        entityManager.clear();

        Member found = memberFinder.find(member.getId());
        entityManager.flush();

        assertThat(found.getId()).isEqualTo(member.getId());

    }

}
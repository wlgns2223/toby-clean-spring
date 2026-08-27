package tobyspring.splearn.application.member.provided;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;
import tobyspring.splearn.SplearnTestConfiguration;
import tobyspring.splearn.domain.MemberFixture;
import tobyspring.splearn.domain.member.Member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Import({SplearnTestConfiguration.class})
class MemberAuthenticatorTest {

    @Autowired
    private MemberAuthenticator memberAuthenticator;

    @Autowired
    private MemberRegister memberRegister;

    @Test
    @DisplayName("로그인 테스트")
    void login() {
        // given
        MemberRegisterRequest registrationRequest = MemberFixture.createMemberRegisterRequest();
        Member member = memberRegister.register(registrationRequest);
        member.activate();

        // when
        Member loggedInmember = memberAuthenticator.login(new MemberLoginRequest(registrationRequest.email(), registrationRequest.password()));

        assertThat(loggedInmember).isEqualTo(member);

    }

    @Test
    @DisplayName("ACTIVE가 아니면 로그인 실패")
    void loginFailedNotActive() {
        MemberRegisterRequest registrationRequest = MemberFixture.createMemberRegisterRequest();
        memberRegister.register(registrationRequest);

        // when
        assertThatThrownBy(() -> memberAuthenticator.login(new MemberLoginRequest(registrationRequest.email(), registrationRequest.password())))
                .isInstanceOf(LoginFailedException.class);
    }

    @Test
    @DisplayName("비밀번호가 틀리면 로그인 실패")
    void loginWithWrongPassword() {
        MemberRegisterRequest registrationRequest = MemberFixture.createMemberRegisterRequest();
        memberRegister.register(registrationRequest);

        // when
        assertThatThrownBy(() -> memberAuthenticator.login(new MemberLoginRequest(registrationRequest.email(), "wrongpassword00")))
                .isInstanceOf(LoginFailedException.class);

    }

}
package tobyspring.splearn.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tobyspring.splearn.application.member.provided.MemberRegisterRequest;
import tobyspring.splearn.domain.member.*;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static tobyspring.splearn.domain.MemberFixture.createMemberRegisterRequest;
import static tobyspring.splearn.domain.MemberFixture.createPasswordEncoder;

class MemberTest {
    Member member;
    PasswordEncoder passwordEncoder;
    private MemberRegisterRequest registerRequest;

    @BeforeEach
    void setUp(){
        passwordEncoder = createPasswordEncoder();
        registerRequest = createMemberRegisterRequest();
        member = Member.register(registerRequest.toInfo(), passwordEncoder);
    }



    @Test
    void registerMember(){
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
        assertThat(member.getDetail().getRegisteredAt()).isNotNull();
    }

    @Test
    void constructorNullCheck() {
        assertThatThrownBy(() -> Member.register(new MemberRegisterRequest(null, "Toby", "secret").toInfo(),passwordEncoder))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void activate() {
        member.activate();
        assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVE);
        assertThat(member.getDetail().getActivatedAt()).isNotNull();
    }

    @Test
    void activateFail() {

        member.activate();

        assertThatThrownBy(() -> {
            member.activate();
        }).isInstanceOf(IllegalStateException.class);

    }

    @Test
    void deactivate() {
        member.activate();

        member.deactivate();

        assertThat(member.getStatus()).isEqualTo(MemberStatus.DEACTIVATED);
        assertThat(member.getDetail().getDeactivatedAt()).isNotNull();
    }

    @Test
    void deactivateFail() {

        assertThatThrownBy(() -> {
            member.deactivate();
        }).isInstanceOf(IllegalStateException.class);

        member.activate();
        member.deactivate();

        assertThatThrownBy(() -> {
            member.deactivate();
        }).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void verifyPassword() {
        assertThat(member.verifyPassword(registerRequest.password(), passwordEncoder)).isTrue();
        assertThat(member.verifyPassword("hello", passwordEncoder)).isFalse();
    }

    @Test
    void changeNickname() {
        assertThat(member.getNickname()).isEqualTo(registerRequest.nickname());

        member.changeNickname("Charlie2");

        assertThat(member.getNickname()).isEqualTo("Charlie2");
    }
    
    @Test
    void changePassword() {
        member.changePassword("verysecret",passwordEncoder);

        assertThat(member.verifyPassword("verysecret", passwordEncoder)).isTrue();
    }

    @Test
    void isActive() {
        assertThat(member.isActive()).isFalse();

        member.activate();

        assertThat(member.isActive()).isTrue();
    }

    @Test
    void invalidEmail() {
        assertThatThrownBy(() -> {
            Member.register(createMemberRegisterRequest("invalid email").toInfo(), passwordEncoder);
        }).isInstanceOf(IllegalArgumentException.class);
        
    }
    
    @Test
    void updateInfo() {
        member.activate();
        MemberInfoUpdateRequest request = new MemberInfoUpdateRequest("leo", "toby100", "introduction");
        member.updateInfo(request);

        assertThat(member.getNickname()).isEqualTo(request.nickname());
        assertThat(member.getDetail().getIntroduction()).isEqualTo(request.introduction());
    }

    @Test
    void updateInfoFail() {
        assertThatThrownBy(() -> {
            member.updateInfo(new MemberInfoUpdateRequest("leo1234", "toby", "introduction"));
        }).isInstanceOf(IllegalStateException.class);
    }

}
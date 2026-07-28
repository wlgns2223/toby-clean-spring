package tobyspring.splearn.domain;

import org.springframework.lang.NonNull;
import org.springframework.test.util.ReflectionTestUtils;
import tobyspring.splearn.application.member.provided.MemberRegisterRequest;
import tobyspring.splearn.domain.member.Member;
import tobyspring.splearn.domain.member.PasswordEncoder;

public class MemberFixture {


    public static MemberRegisterRequest createMemberRegisterRequest(String email) {
        return new MemberRegisterRequest(email, "Charlie", "verysecret");
    }

    @NonNull
    public static MemberRegisterRequest createMemberRegisterRequest() {
        return createMemberRegisterRequest("toby@splearn.app");
    }

    public static Member createMember(){
        return Member.register(createMemberRegisterRequest().toInfo(),createPasswordEncoder());
    }

    public static Member createMember(Long memberId){
        Member member = Member.register(createMemberRegisterRequest().toInfo(),createPasswordEncoder());
        ReflectionTestUtils.setField(member,"id",memberId);
        return member;
    }


    @NonNull
    public static PasswordEncoder createPasswordEncoder() {
        return new PasswordEncoder() {
            @Override
            public String encode(String password) {
                return password.toUpperCase();
            }

            @Override
            public boolean matches(String password, String passwordHash) {
                return encode(password).equals(passwordHash);
            }
        };
    }
}

package tobyspring.splearn.domain;

import org.instancio.Instancio;
import org.instancio.Select;
import org.springframework.lang.NonNull;
import org.springframework.test.util.ReflectionTestUtils;
import tobyspring.splearn.application.member.provided.MemberRegisterRequest;
import tobyspring.splearn.domain.member.Member;
import tobyspring.splearn.domain.member.PasswordEncoder;

public class MemberFixture {


    public static MemberRegisterRequest createMemberRegisterRequest(String email) {
        return Instancio.of(MemberRegisterRequest.class)
                .set(Select.field(MemberRegisterRequest::email), email)
                .create();

    }

    @NonNull
    public static MemberRegisterRequest createMemberRegisterRequest() {
        return createMemberRegisterRequest(Instancio.gen().net().email().get());
    }

    public static Member createMember(){
        return Member.register(createMemberRegisterRequest().toInfo(),createPasswordEncoder());
    }

    public static Member createMember(Long memberId){
        Member member = Member.register(createMemberRegisterRequest().toInfo(),createPasswordEncoder());
        ReflectionTestUtils.setField(member,"id",memberId);
        return member;
    }

    public static Member createActiveMember(){
        Member member = createMember();
        member.activate();
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

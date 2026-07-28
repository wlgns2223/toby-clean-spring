package tobyspring.splearn.application.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import tobyspring.splearn.application.member.provided.LoginFailedException;
import tobyspring.splearn.application.member.provided.MemberAuthenticator;
import tobyspring.splearn.application.member.provided.MemberLoginRequest;
import tobyspring.splearn.application.member.required.MemberRepository;
import tobyspring.splearn.domain.member.Member;
import tobyspring.splearn.domain.member.PasswordEncoder;
import tobyspring.splearn.domain.shared.Email;

@Service
@Transactional
@Validated
@RequiredArgsConstructor
public class MemberAuthenticationService implements MemberAuthenticator {

    /*
    MemberFinder를 사용해도 되지만, 로그인시 Member를 찾을때에는 부가 로직이 들어갈 가능성이 많다.
    그것을 MemberFinder에 구현하기에는 적절하지 않으니, MemberRepository를 받는다.
     */
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Member login(MemberLoginRequest memberLoginRequest) throws LoginFailedException {
        Member member = memberRepository.findByEmail(new Email(memberLoginRequest.email()))
                .orElseThrow(LoginFailedException::new);

        if(!member.isActive()){
            throw new LoginFailedException();
        }

        if(!member.verifyPassword(memberLoginRequest.password(),passwordEncoder)){
            throw new LoginFailedException();
        }

        return member;
    }
}

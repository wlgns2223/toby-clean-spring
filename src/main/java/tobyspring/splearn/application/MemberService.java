package tobyspring.splearn.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tobyspring.splearn.application.provided.MemberRegister;
import tobyspring.splearn.application.required.EmailSender;
import tobyspring.splearn.application.required.MemberRepository;
import tobyspring.splearn.domain.Member;
import tobyspring.splearn.domain.MemberRegisterRequest;
import tobyspring.splearn.domain.PasswordEncoder;

@Service
@RequiredArgsConstructor
public class MemberService implements MemberRegister {

    private final MemberRepository memberRepository;
    private final EmailSender emailSender;
    private final PasswordEncoder passwordEncoder;

    /**
     * 애플리케이션 서비스는 어떤 절차를 따르는지 코드에 잘 드러나야한다. 잘 읽혀야한다.
     */
    @Override
    public Member register(MemberRegisterRequest registerRequest) {
        // 검사

        // domain model
        Member member = Member.register(registerRequest, passwordEncoder);
        // repository 저장
        memberRepository.save(member);
        // 후처리 작업
        emailSender.send(member.getEmail(),"등록을 완료해 주세요", "아래의 링크를 클릭해서 등록을 완료해 주세요");

        return member;
    }
}

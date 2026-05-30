package tobyspring.splearn.application.member;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import tobyspring.splearn.application.member.provided.MemberFinder;
import tobyspring.splearn.application.member.provided.MemberRegister;
import tobyspring.splearn.application.member.required.EmailSender;
import tobyspring.splearn.application.member.required.MemberRepository;
import tobyspring.splearn.domain.member.*;
import tobyspring.splearn.domain.shared.Email;

@Service
@RequiredArgsConstructor
@Validated
public class MemberModifyService implements MemberRegister {

    /**
     * 단순히 조회로직을 모아 놓은 포트이기 때문에 자기 자신의 포트를 이용해서 memberFinder를 사용할 수 있다.
     * 조회와 변경이 분리 된 경우 이렇게 해도 됨
     */
    private final MemberFinder memberFinder;
    private final MemberRepository memberRepository;
    private final EmailSender emailSender;
    private final PasswordEncoder passwordEncoder;

    /**
     * 애플리케이션 서비스는 어떤 절차를 따르는지 코드에 잘 드러나야한다. 잘 읽혀야한다.
     * 이걸 읽을때 기술적인 내용 <-> 비즈니스 로직적인 내용으로 관심사가 왔다갔다 한다면 리팩토링이 필요하다.
     * 코드가 잘 안읽힌다는 뜻임.
     */
    @Override
    @Transactional
    public Member register(MemberRegisterRequest registerRequest) {
        // 검사
        checkDuplicateEmail(registerRequest);

        // domain model
        Member member = Member.register(registerRequest, passwordEncoder);
        // repository 저장
        memberRepository.save(member);
        // 후처리 작업

        /**
         * 이메일을 보낼때 첫번째 파라미터는 뭐고..제목을 어떻게 보내고..는 디테일한 내용이다.
         * 디테일은 숨기고 최상위 public method는 문서처럼 읽혀야한다.
         */
        sendWelcomeEmail(member);

        return member;
    }

    @Override
    @Transactional
    public Member activate(Long memberId) {
        Member member = memberFinder.find(memberId);

        member.activate();

        // jpa가 아니라 spring data를 사용하는 경우에는 save를 해줘야한다.
        // spring data는 domain event publication을 한다. save를 하면 event publication 발생
        return memberRepository.save(member);
    }

    private void sendWelcomeEmail(Member member) {

        emailSender.send(member.getEmail(),"등록을 완료해 주세요", "아래의 링크를 클릭해서 등록을 완료해 주세요");
    }

    private void checkDuplicateEmail(MemberRegisterRequest registerRequest) {
        if(memberRepository.findByEmail(new Email(registerRequest.email())).isPresent()){
            throw new DuplicateEmailException("이미 사용중인 이메일 입니다.");
        }
    }

    @Override
    public Member deactivate(Long memberId) {
        Member member = memberFinder.find(memberId);

        member.deactivate();

        return memberRepository.save(member);
    }

    @Override
    public Member updateInfo(Long memberId,MemberInfoUpdateRequest request) {
        Member member = memberFinder.find(memberId);

        checkDuplicateProfile(member, request.profileAddress());


        member.updateInfo(request);

        return memberRepository.save(member);
    }

    private void checkDuplicateProfile(Member member,  String profile) {
        if(profile.isEmpty()) return;
        Profile currentProfile = member.getDetail().getProfile();
        if(currentProfile != null && currentProfile.value().equals(profile)) return;

        if(memberRepository.findByProfile((new Profile(profile))).isPresent()){
            throw new IllegalArgumentException("이미 존재하는 프로필 주소입니다.");
        }
    }
}

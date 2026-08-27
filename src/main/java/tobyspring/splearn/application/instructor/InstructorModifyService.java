package tobyspring.splearn.application.instructor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import tobyspring.splearn.application.instructor.provided.DuplicationInstructorApplicationException;
import tobyspring.splearn.application.instructor.provided.InstructorApplication;
import tobyspring.splearn.application.instructor.provided.InstructorApplyRequest;
import tobyspring.splearn.application.instructor.provided.InstructorFinder;
import tobyspring.splearn.application.instructor.required.InstructorRepository;
import tobyspring.splearn.application.member.provided.MemberFinder;
import tobyspring.splearn.domain.instructor.Instructor;
import tobyspring.splearn.domain.member.Member;

@Service
@Validated
@Transactional
@RequiredArgsConstructor
public class InstructorModifyService implements InstructorApplication {

    private final InstructorRepository instructorRepository;
    private final MemberFinder memberFinder;
    private final InstructorFinder instructorFinder;

    @Override
    public Instructor apply(InstructorApplyRequest applyRequest) {
        // member를 찾아오고
        Member member = memberFinder.find(applyRequest.memberId());

        checkDuplicationApplication(member);

        // instructor를 만들고
        Instructor instructor = Instructor.apply(member);

        // instructor를 저장
        return instructorRepository.save(instructor);
    }

    private void checkDuplicationApplication(Member member) {
        if(instructorRepository.findByMemberId(member.getId()).isPresent()){
            throw new DuplicationInstructorApplicationException("강사 신청이 중복되었습니다.");
        }
    }

    @Override
    public Instructor approve(Long instructorId) {
        Instructor instructor = instructorFinder.find(instructorId);
        instructor.approve();
        return instructorRepository.save(instructor);
    }

    @Override
    public Instructor reject(Long instructorId) {
        Instructor instructor = instructorFinder.find(instructorId);
        instructor.reject();
        return instructorRepository.save(instructor);
    }
}

package tobyspring.splearn.domain.member;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.NaturalIdCache;
import org.springframework.util.Assert;
import tobyspring.splearn.domain.AbstractEntity;
import tobyspring.splearn.domain.shared.Email;

import java.util.Objects;

import static java.util.Objects.requireNonNull;
import static org.springframework.util.Assert.state;

@Entity
@Getter
@ToString(callSuper = true,exclude = "detail")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@NaturalIdCache // 같은 트랜잭션안에서 같은 아이디로 읽을때 영속 컨텍스트안에서 읽어옴. 이메일로 읽어올때도.
public class Member extends AbstractEntity {

    @Embedded
    @NaturalId // hibernate 지원, unique 조건 걸어줌
    private Email email;

    @Column(length = 100,nullable = false)
    private String nickname;

    @Column(length = 200,nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    private MemberStatus status;

    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private MemberDetail detail;

    public static Member register(MemberRegisterRequest createRequest, PasswordEncoder passwordEncoder){
        Member member = new Member();

        String email = createRequest.email();

        member.email = new Email(requireNonNull(email));
        member.nickname = requireNonNull(createRequest.nickname());
        member.passwordHash = passwordEncoder.encode(requireNonNull(createRequest.password()));
        member.status = MemberStatus.PENDING;
        member.detail = MemberDetail.create();

        return member;
    }

    public void activate() {
        state(status == MemberStatus.PENDING,"PENDING 상태가 아닙니다.");

        this.status = MemberStatus.ACTIVE;
        this.detail.setActivatedAt();
    }

    public void deactivate() {
        state(status == MemberStatus.ACTIVE,"Active 상태가 아닙니다.");

        this.status = MemberStatus.DEACTIVATED;
        this.detail.deactivate();
    }

    public boolean verifyPassword(String password, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(password, this.passwordHash);
    }

    public void changeNickname(String nickname) {
        this.nickname = requireNonNull(nickname);
    }

    public void updateInfo(MemberInfoUpdateRequest request){
        Assert.state(getStatus() == MemberStatus.ACTIVE,"등록 완료 상태가 아니면 정보를 수정할 수 없습니다.");
        this.nickname = Objects.requireNonNull(request.nickname());
        this.detail.updateInfo(request);

    }

    public void changePassword(String password,PasswordEncoder passwordEncoder) {

        this.passwordHash = passwordEncoder.encode(requireNonNull(password));

    }

    public boolean isActive() {
        return this.status.equals(MemberStatus.ACTIVE);
    }
}

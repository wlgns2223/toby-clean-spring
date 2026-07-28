package tobyspring.splearn.domain.member;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.util.Assert;
import tobyspring.splearn.domain.AbstractEntity;

import java.time.LocalDateTime;
import java.util.Objects;


@Entity
@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberDetail extends AbstractEntity {

    @Embedded
    private Profile profile;

    @Column(columnDefinition = "TEXT")
    private String introduction;

    @Column(nullable = false)
    private LocalDateTime registeredAt;

    private LocalDateTime activatedAt;

    private LocalDateTime deactivatedAt;

    static MemberDetail create() {
        // 애그리게이트 루트에서만 사용할 수 있고 다른 애그리게이트 (다른 패키지)에서는 사용못하도록 패키지 제한자 package private
        MemberDetail detail = new MemberDetail();
        detail.registeredAt = LocalDateTime.now();
        return detail;
    }


    void setActivatedAt() {
        Assert.isTrue(activatedAt == null,"이미 activatedAt은 설정하였습니다.");
        this.activatedAt = LocalDateTime.now();
    }

    void deactivate() {
        Assert.isTrue(deactivatedAt == null, "이미 deactivate 되지 않았습니다.");
        this.deactivatedAt = LocalDateTime.now();
    }

    void updateInfo(MemberInfoUpdateRequest request) {
        this.profile = convertToProfile(request.profileAddress());
        this.introduction = Objects.requireNonNull(request.introduction());
    }

    private Profile convertToProfile(String profileAddress) {

        if(profileAddress == null || profileAddress.isEmpty()) return null;

        return new Profile(profileAddress);
    }
}

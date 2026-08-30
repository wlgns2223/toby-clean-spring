package tobyspring.splearn.domain.course;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import tobyspring.splearn.domain.AbstractEntity;
import tobyspring.splearn.domain.instructor.Instructor;

import java.util.Objects;

@Entity
@Getter
@ToString(callSuper = true,exclude = {})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Course extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    private Instructor instructor;

    @Column(nullable = false,length = 100)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,length = 20)
    private CourseStatus status;

    @OneToOne(optional = false,cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private CourseDetail detail;

    public Course(Instructor instructor, String title, @Nullable String description) {
        // 도메인 규칙
        instructor.ensureActive();

        this.instructor = Objects.requireNonNull(instructor);
        this.title = Objects.requireNonNull(title);
        this.status = CourseStatus.DRAFT;
        this.detail = new CourseDetail(description);
    }

    public void submitForReview() {
        Assert.state(status == CourseStatus.DRAFT,"DRAFT 상태이어야 합니다.");
        Assert.state(StringUtils.hasText(detail.getDescription()),"강의 소개가 등록되지 않았습니다.");
        this.status = CourseStatus.IN_REVIEW;
    }

    public void publish() {
        Assert.state(status == CourseStatus.IN_REVIEW, "REVIEW 상태이어야 합니다.");

        this.status = CourseStatus.PUBLISHED;
        this.detail.publish();
    }

    public void archive(){
        Assert.state(status == CourseStatus.PUBLISHED,"PUBLISH 상태이어야 합니다.");
        this.status = CourseStatus.ARCHIVED;
        this.detail.archive();

    }

    public boolean isPublished(){
        return status == CourseStatus.PUBLISHED;
    }

    public void ensurePublished(){
        Assert.state(status == CourseStatus.PUBLISHED, "PUBLISHED 상태가 아닙니다.");
    }

    public void updateInfo(CourseUpdateInfo updateInfo){
        this.title = Objects.requireNonNull(updateInfo.title());
        this.detail.updateInfo(updateInfo);

    }
}

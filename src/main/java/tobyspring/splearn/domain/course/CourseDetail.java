package tobyspring.splearn.domain.course;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import tobyspring.splearn.domain.AbstractEntity;

import java.time.LocalDateTime;

@Entity
@Getter
@ToString(callSuper = true,exclude = {})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CourseDetail extends AbstractEntity {

    @Column(length = 100)
    private String description;

    private LocalDateTime createdAt;

    private LocalDateTime publishedAt;

    private LocalDateTime archivedAt;

    CourseDetail(String description) {
        this.description = description;
        this.createdAt = LocalDateTime.now();
    }

    public void publish() {
        this.publishedAt = LocalDateTime.now();
    }

    public void archive(){
        this.archivedAt = LocalDateTime.now();
    }

    public void updateInfo(CourseUpdateInfo updateInfo) {
        this.description = updateInfo.description();
    }
}

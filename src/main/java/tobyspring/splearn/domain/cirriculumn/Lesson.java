package tobyspring.splearn.domain.cirriculumn;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import tobyspring.splearn.domain.AbstractEntity;

import java.util.Objects;

@Entity
@Getter
@ToString(callSuper = true,exclude = {"section"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Lesson extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    private Section section;

    @Column(nullable = false)
    private String title;

    Lesson(Section section, String title) {
        this.section = section;
        this.title = Objects.requireNonNull(title);
    }

    void updateTitle(String newTitle) {
        this.title = Objects.requireNonNull(newTitle);
    }

    public void moveTo(Section section) {
        this.section = section;
    }
}

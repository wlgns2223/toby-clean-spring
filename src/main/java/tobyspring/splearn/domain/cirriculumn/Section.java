package tobyspring.splearn.domain.cirriculumn;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import tobyspring.splearn.domain.AbstractEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@ToString(callSuper = true,exclude = {"curriculum","lessons"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Section extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    private Curriculum curriculum;

    @Column(nullable = false)
    private String title;

    @Getter(AccessLevel.NONE)
    @OrderColumn(name = "lesson_order")
    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL,orphanRemoval = true)
    final private List<Lesson> lessons = new ArrayList<>();

    public List<Lesson> getLessons() {
        return Collections.unmodifiableList(lessons);
    }

    Section(Curriculum curriculum, String title) {
        this.curriculum = curriculum;
        this.title = Objects.requireNonNull(title);
    }

    Lesson addLesson(String title){
        Lesson lesson = new Lesson(this, title);
        this.lessons.add(lesson);
        return lesson;
    }

    void updateTitle(String newTitle) {
        this.title = Objects.requireNonNull(newTitle);
    }

    void updateLessonTitle(int lessonIndex,String newTitle){
        this.lessons.get(lessonIndex).updateTitle(newTitle);
    }

    Lesson removeLesson(int lessonIndex){
        return this.lessons.remove(lessonIndex);

    }

    void moveAllLessonsTo(Section target,int insertIndex) {
        while (!this.lessons.isEmpty()){
            target.addLesson(insertIndex++,this.lessons.getFirst());
            this.lessons.removeFirst();
        }
    }

    void addLesson(int insertIndex,Lesson lesson){
        lesson.moveTo(this);
        this.lessons.add(insertIndex,lesson);
    }


}

package tobyspring.splearn.domain.cirriculumn;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.util.Assert;
import tobyspring.splearn.domain.AbstractEntity;
import tobyspring.splearn.domain.course.Course;

import java.util.*;

@Entity
@Getter
@ToString(callSuper = true, exclude = {"sections", "course"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Curriculum extends AbstractEntity {

    @OneToOne(optional = false,fetch = FetchType.LAZY)
    private Course course;

    @Getter(AccessLevel.NONE)
    @OrderColumn(name = "section_order")
    @OneToMany(mappedBy = "curriculum",cascade = CascadeType.ALL)
    final private List<Section> sections = new ArrayList<>();

    public List<Section> getSections() {
        return Collections.unmodifiableList(sections);
    }

    public Curriculum(Course course) {
        this.course = Objects.requireNonNull(course);
    }

    public Section addSection(String title) {
        Section section = new Section(this, title);
        this.sections.add(section);
        return section;
    }

    public Section addSection(int index,String title) {
        Objects.checkIndex(index, sections.size() + 1);

        Section section = new Section(this, title);
        this.sections.add(index, section);
        return section;
    }

    public Lesson addLesson(int sectionIndex, String title) {
        return this.sections.get(sectionIndex).addLesson(title);
    }

    public Section updateSectionTitle(int sectionIndex, String newTitle) {
        Section section = this.sections.get(sectionIndex);
        section.updateTitle(newTitle);
        return section;
    }

    public Lesson updateLessonTitle(int sectionIndex, int lessonIndex, String newTitle) {
        Section section = this.sections.get(sectionIndex);
        Lesson lesson = section.getLessons().get(lessonIndex);
        lesson.updateTitle(newTitle);
        return lesson;
    }

    public Lesson removeLesson(int sectionIndex, int lessonIndex) {
        Section section = this.sections.get(sectionIndex);
        return section.getLessons().remove(lessonIndex);
    }

    public List<Lesson> allLessons(){
        return this.getSections().stream().flatMap(section -> section.getLessons().stream()).toList();
    }

    public void removeSection(int sectionIndex) {

        Assert.state(this.sections.size() > 1, "마지막 남은 섹션은 삭제할 수 없습니다.");

        Section remove = this.sections.remove(sectionIndex);

        if(sectionIndex == 0){

            Section next = this.sections.get(0);
            remove.moveAllLessonsTo(next,0);

        } else {
            Section previous = this.sections.get(sectionIndex - 1);
            remove.moveAllLessonsTo(previous,previous.getLessons().size());
        }
    }

    public void moveLesson(int fromSectionIndex, int fromLessonIndex, int toSectionIndex, int toLessonIndex){
        Section from = this.sections.get(fromSectionIndex);
        Section to = this.sections.get(toSectionIndex);

        Lesson lesson = from.removeLesson(fromLessonIndex);
        to.addLesson(toLessonIndex,lesson);
    }

    public void validate(){
        if(this.sections.isEmpty()) throw new RuntimeException("최소한 하나의 섹션이 필요합니다.");
        this.sections.forEach(s -> {
            if(s.getLessons().isEmpty()) throw new RuntimeException("수없이 없는 섹션은 허용되지 않습니다.");
        });
    }

    public Optional<Lesson> firstLesson() {
        return this.allLessons().stream().findFirst();

    }

    public Optional<Lesson> nextLesson(Lesson lesson) {
        List<Lesson> lessons = allLessons();
        int i = lessons.indexOf(lesson);
        Assert.state(i >=0 , "커리큘럼에 포함된 수업이 아닙니다.");

        if(i + 1 >= lessons.size()) return Optional.empty();

        return Optional.of(lessons.get(i + 1));
    }

    public Optional<Lesson> nextLesson(Long lessonId){
        List<Lesson> lessons = allLessons();
        Lesson lesson = lessons.stream().filter(l -> lessonId.equals(l.getId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("아이디에 맞는 레슨이 없습니다."));

        return nextLesson(lesson);
    }
}

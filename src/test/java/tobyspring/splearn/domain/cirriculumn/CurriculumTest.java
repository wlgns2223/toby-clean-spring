package tobyspring.splearn.domain.cirriculumn;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tobyspring.splearn.domain.course.Course;
import tobyspring.splearn.domain.course.CourseFixture;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CurriculumTest {

    @Test
    @DisplayName("create")
    void create() {
        // given
        Course course = CourseFixture.createCourse();

        Curriculum curriculum = new Curriculum(course);

        assertThat(curriculum).isNotNull();
        assertThat(curriculum.getCourse()).isEqualTo(course);

    }

    @Test
    @DisplayName("addSection")
    void addSection() {
        Curriculum curriculum = CurriculumFixture.createCurriculum();

        Section section = curriculum.addSection("Section 1");

        assertThat(curriculum.getSections()).containsExactly(section);
        assertThat(curriculum.getSections()).extracting(Section::getTitle)
                .containsExactly("Section 1");

    }
    
    @Test
    @DisplayName("add secion index")
    void addSectionWithIndex() {
        Curriculum curriculum = CurriculumFixture.createCurriculum();

        Section section1 = curriculum.addSection("s1");
        Section section2 = curriculum.addSection("s2");

        assertThat(curriculum.getSections()).containsExactly(section1, section2);

        Section section1_1 = curriculum.addSection(1,"s1-1");

        assertThat(curriculum.getSections()).containsExactly(section1, section1_1, section2);

        Section s3 = curriculum.addSection(3,"s3");

        assertThat(curriculum.getSections()).containsExactly(section1, section1_1, section2, s3);

    }

    @Test
    @DisplayName("add lesson")
    void adddLesson() {
        Curriculum curriculum = CurriculumFixture.createCurriculum();
        Section s0 = curriculum.addSection("s0");
        Section s1 = curriculum.addSection("s1");

        Lesson l0 = curriculum.addLesson(0, "l0");
        Lesson l1 = curriculum.addLesson(1, "l1");

        assertThat(s0.getLessons()).containsExactly(l0);
        assertThat(s1.getLessons()).containsExactly(l1);

    }
    
    @Test
    @DisplayName("update section title")
    void updateSectionTitle() {
        Curriculum curriculum = CurriculumFixture.createCurriculum();
        Section s0 = curriculum.addSection("s0");
        Section s1 = curriculum.addSection("s1");

        curriculum.updateSectionTitle(0, "S0 Updated");

        assertThat(s0.getTitle()).isEqualTo("S0 Updated");

    }

    @Test
    @DisplayName("update lesson title")
    void updateLessonTitle() {
        Curriculum curriculum = CurriculumFixture.createCurriculum();
        Section s0 = curriculum.addSection("s0");
        Section s1 = curriculum.addSection("s1");
        Lesson l0 = curriculum.addLesson(0, "L0");
        Lesson l1 = curriculum.addLesson(1, "L1");

        curriculum.updateLessonTitle(0, 0, "L0 Updated");

        assertThat(l0.getTitle()).isEqualTo("L0 Updated");
    }

    @Test
    @DisplayName("remove lesson")
    void removeLesson() {
        Curriculum curriculum = CurriculumFixture.createCurriculum();
        Section s0 = curriculum.addSection("S0");
        Section s1 = curriculum.addSection("S1");
        Lesson l0 = curriculum.addLesson(0, "L0");
        Lesson l1 = curriculum.addLesson(1, "L1");

        assertThat(curriculum.allLessons()).extracting(Lesson::getTitle).containsExactly("L0", "L1");

        curriculum.removeLesson(0, 0);

        assertThat(curriculum.allLessons()).extracting(Lesson::getTitle).containsExactly("L1");
    }

    @Test
    @DisplayName("remove section")
    void removeSection() {
        Curriculum curriculum = CurriculumFixture.createCurriculum();
        Section s0 = curriculum.addSection("S0");
        Section s1 = curriculum.addSection("S1");
        Section s2 = curriculum.addSection("S2");
        Lesson l0 = curriculum.addLesson(0, "L0");
        Lesson l1 = curriculum.addLesson(1, "L1");
        Lesson l2 = curriculum.addLesson(2, "L2");

        assertThat(SectionContent.from(curriculum)).containsExactly(
                new SectionContent(
                        "S0",
                        List.of(new LessonContent("L0"))
                ),
                new SectionContent(
                        "S1",
                        List.of(new LessonContent("L1"))
                ),
                new SectionContent(
                        "S2",
                        List.of(new LessonContent("L2"))
                )
        );

        curriculum.removeSection(2);

        assertThat(curriculum.getSections()).containsExactly(s0,s1);
        assertThat(SectionContent.from(curriculum)).containsExactly(

                new SectionContent(
                        "S0",
                        List.of(new LessonContent("L0"))
                ),
                new SectionContent(
                        "S1",
                        List.of(new LessonContent("L1"),new LessonContent("L2"))
                )
        );

        curriculum.removeSection(0);
        assertThat(curriculum.getSections()).containsExactly(s1);
        assertThat(SectionContent.from(curriculum)).containsExactly(
                new SectionContent(
                        "S1",
                        List.of(new LessonContent("L0"), new LessonContent("L1"), new LessonContent("L2"))
                )
        );

    }

    @Test
    @DisplayName("move lesson")
    void moveLesson() {
        // given

        // when

        // then

    }

    @Test
    @DisplayName("first lesson")
    void firstLesson() {

        Curriculum curriculum = CurriculumFixture.createCurriculum();
        Section s0 = curriculum.addSection("S0");
        Section s1 = curriculum.addSection("S1");
        Section s2 = curriculum.addSection("S2");
        Lesson l0 = curriculum.addLesson(0, "L0");
        Lesson l1 = curriculum.addLesson(1, "L1");
        Lesson l2 = curriculum.addLesson(2, "L2");
        Lesson lesson = curriculum.firstLesson().orElseThrow();

        assertThat(lesson).isEqualTo(l0);
        assertThat(curriculum.nextLesson(lesson).orElseThrow()).isEqualTo(l1);

    }

}
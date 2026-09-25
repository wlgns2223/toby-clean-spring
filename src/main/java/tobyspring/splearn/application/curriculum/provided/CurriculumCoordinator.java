package tobyspring.splearn.application.curriculum.provided;

import tobyspring.splearn.application.course.required.CurriculumCreator;
import tobyspring.splearn.domain.cirriculumn.Curriculum;

public interface CurriculumCoordinator extends CurriculumCreator {

    Curriculum addSection(Long curriculumId, String title);

    Curriculum addSection(Long curriculumId, int sectionIndex, String title);

    Curriculum addLesson(Long curriculumId, int sectionIndex, String title);

    Curriculum updateSectionTitle(Long curriculumId, int sectionIndex, String title);

    Curriculum updateLessonTitle(Long curriculumId, int sectionIndex, int lessonIndex, String title);

    Curriculum removeLesson(Long curriculumId, int sectionIndex, int lessonIndex);

    Curriculum removeSection(Long curriculumId, int sectionIndex);

    Curriculum moveLesson(Long curriculumId, int fromSectionIndex, int fromLessonIndex, int toSectionIndex, int toLessonIndex);

    Curriculum validate(Long curriculumId);

}

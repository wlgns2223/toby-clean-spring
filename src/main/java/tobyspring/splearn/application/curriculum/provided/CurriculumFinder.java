package tobyspring.splearn.application.curriculum.provided;

import tobyspring.splearn.domain.cirriculumn.Curriculum;
import tobyspring.splearn.domain.cirriculumn.Lesson;

import java.util.Optional;

public interface CurriculumFinder {
    Curriculum find(Long curriculumId);

    Curriculum findByCourse(Long courseId);

    Curriculum findWithSections(Long curriculumId);

    Optional<Lesson> firstLesson(Long curriculumId);

    Optional<Lesson> nextLesson(Long curriculumId, Long lessonId);
}

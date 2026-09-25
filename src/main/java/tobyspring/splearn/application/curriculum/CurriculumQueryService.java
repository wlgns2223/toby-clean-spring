package tobyspring.splearn.application.curriculum;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import tobyspring.splearn.application.curriculum.provided.CurriculumFinder;
import tobyspring.splearn.application.curriculum.required.CurriculumRepository;
import tobyspring.splearn.domain.cirriculumn.Curriculum;
import tobyspring.splearn.domain.cirriculumn.Lesson;

import java.util.Optional;

@Service
@Validated
@Transactional
@RequiredArgsConstructor
public class CurriculumQueryService implements CurriculumFinder {

    private final CurriculumRepository curriculumRepository;

    @Override
    public Curriculum findWithSections(Long curriculumId) {
        return this.curriculumRepository.findWithSectionsById(curriculumId)
                .orElseThrow(() -> new IllegalArgumentException("커리큘럼을 찾을 수 없습니다. ID: " + curriculumId));
    }

    @Override
    public Curriculum find(Long curriculumId) {
        return this.curriculumRepository.findById(curriculumId)
                .orElseThrow(() -> new IllegalArgumentException("커리큘럼을 찾을 수 없습니다. ID: " + curriculumId));
    }

    @Override
    public Curriculum findByCourse(Long courseId) {
        return this.curriculumRepository.findByCourseId(courseId)
                .orElseThrow(() -> new IllegalArgumentException("커리큘럼을 찾을 수 없습니다. ID: " + courseId));
    }

    @Override
    public Optional<Lesson> firstLesson(Long curriculumId) {
        return this.findWithSections(curriculumId).firstLesson();
    }

    @Override
    public Optional<Lesson> nextLesson(Long curriculumId, Long lessonId) {
        return this.findWithSections(curriculumId).nextLesson(lessonId);
    }
}

package tobyspring.splearn.application.curriculum.required;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.Repository;
import tobyspring.splearn.domain.cirriculumn.Curriculum;

import java.util.Optional;

public interface CurriculumRepository extends Repository<Curriculum,Long> {

    Curriculum save(Curriculum curriculum);

    @EntityGraph(attributePaths = {"sections", "sections.lessons"})
    Optional<Curriculum> findWithSectionsById(Long curriculumId);

    Optional<Curriculum> findById(Long curriculumId);

    Optional<Curriculum> findByCourseId(Long courseId);
}

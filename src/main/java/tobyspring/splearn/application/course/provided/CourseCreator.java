package tobyspring.splearn.application.course.provided;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import tobyspring.splearn.domain.course.Course;

public interface CourseCreator {
    Course create(@Valid CourseCreateRequest createRequest) throws ValidationException;

    Course updateInfo(Long courseId, @Valid CourseInfoUpdateRequest infoUpdateRequest);
}

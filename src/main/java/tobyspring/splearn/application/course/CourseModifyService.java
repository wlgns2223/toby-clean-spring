package tobyspring.splearn.application.course;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import tobyspring.splearn.application.course.provided.*;
import tobyspring.splearn.application.course.required.CourseRepository;
import tobyspring.splearn.application.instructor.provided.InstructorFinder;
import tobyspring.splearn.domain.course.Course;
import tobyspring.splearn.domain.instructor.Instructor;

@Service
@Transactional
@RequiredArgsConstructor
@Validated
public class CourseModifyService implements CourseCreator {

    private final CourseRepository courseRepository;
    private final CourseFinder courseFinder;
    private final InstructorFinder instructorFinder;
    private final CourseValidator courseValidator;

    @Override
    public Course create(CourseCreateRequest createRequest) {
        Instructor instructor = instructorFinder.find(createRequest.instructorId());
        courseValidator.validateForCreate(instructor,createRequest);
        Course course = new Course(instructor, createRequest.title(), createRequest.description());
        return courseRepository.save(course);
    }

    @Override
    public Course updateInfo(Long courseId, CourseInfoUpdateRequest infoUpdateRequest) {
        Course course = courseFinder.find(courseId);
        courseValidator.validateForUpdate(course,infoUpdateRequest);
        course.updateInfo(infoUpdateRequest.toInfo());

        return courseRepository.save(course);

    }
}

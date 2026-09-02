package tobyspring.splearn.domain.course;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import org.instancio.Instancio;
import org.instancio.InstancioApi;
import org.instancio.Select;
import tobyspring.splearn.application.course.provided.CourseCreateRequest;
import tobyspring.splearn.application.course.provided.CourseInfoUpdateRequest;
import tobyspring.splearn.domain.instructor.Instructor;
import tobyspring.splearn.domain.instructor.InstructorFixture;

import java.time.LocalDateTime;

import static org.instancio.Instancio.gen;

public class CourseFixture {
    public static Course createCourse(@Nullable Instructor instructor, @Nullable String title){

        CourseDetail detail = Instancio.of(CourseDetail.class)
                .ignore(Select.field(CourseDetail::getId))
                .generate(Select.field(CourseDetail::getDescription), gen -> gen.string().maxLength(100))
                .set(Select.field(CourseDetail::getCreatedAt), LocalDateTime.now())
                .create();

        return Instancio.of(Course.class)
                .ignore(Select.field(Course::getId))
                .set(Select.field(Course::getTitle), title == null
                        ?  gen().string().maxLength(100).minLength(2).get()
                        : title)
                .set(Select.field(Course::getStatus),CourseStatus.DRAFT)
                .set(Select.field(Course::getInstructor),instructor == null
                                ? InstructorFixture.createActiveInstructor()
                                : instructor
                        )
                .set(Select.field(Course::getDetail),detail)
                .create();
    }

    public static Course createCourse(){
        return createCourse(null,null);
    }

    public static CourseCreateRequest createCourseCreateRequest(Long instructorId,@Nullable String title) {

        return Instancio.of(CourseCreateRequest.class)
                .set(Select.field(CourseCreateRequest::instructorId), instructorId)
                .set(Select.field(CourseCreateRequest::title), title == null
                        ? gen().string().maxLength(100).minLength(2).get()
                        : title)
                .generate(Select.field(CourseCreateRequest::description), gen -> gen.string().maxLength(100))
                .create();
    }

    public static CourseInfoUpdateRequest createCourseInfoUpdateRequest(@Nullable String title) {

        return Instancio.of(CourseInfoUpdateRequest.class)
                .set(Select.field(CourseInfoUpdateRequest::title), title == null
                        ? gen().string().maxLength(100).minLength(2).get()
                        : title)
                .generate(Select.field(CourseInfoUpdateRequest::description), gen -> gen.string().maxLength(100))
                .create();

    }

    public static Course createPublishedCourse() {
        Course course = createCourse();
        course.updateInfo(createCourseInfoUpdateRequest(course.getTitle()).toInfo());
        course.submitForReview();
        course.publish();
        return course;
    }
}

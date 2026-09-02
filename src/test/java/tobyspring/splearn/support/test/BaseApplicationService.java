package tobyspring.splearn.support.test;

import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import tobyspring.splearn.application.course.provided.CourseCreator;
import tobyspring.splearn.application.enrollment.provided.EnrollRequest;
import tobyspring.splearn.application.enrollment.provided.Enroller;
import tobyspring.splearn.application.instructor.provided.InstructorApplication;
import tobyspring.splearn.application.member.provided.MemberRegister;
import tobyspring.splearn.domain.MemberFixture;
import tobyspring.splearn.domain.course.Course;
import tobyspring.splearn.domain.course.CourseFixture;
import tobyspring.splearn.domain.enrollment.Enrollment;
import tobyspring.splearn.domain.instructor.Instructor;
import tobyspring.splearn.domain.instructor.InstructorFixture;
import tobyspring.splearn.domain.member.Member;

@SpringBootTest
@Transactional
public class BaseApplicationService {

    @Autowired
    MemberRegister memberRegister;

    @Autowired
    InstructorApplication instructorApplication;

    @Autowired
    CourseCreator courseCreator;

    @Autowired
    Enroller enroller;

    protected Member member;
    protected Instructor instructor;
    protected Course course;
    protected Enrollment enrollment;

    @NonNull
    protected Instructor prepareInstructor() {
        this.member = prepareActiveMember();
        this.instructor = instructorApplication.apply(InstructorFixture.createApplyRequest(member));

        instructor.approve();
        return this.instructor;
    }

    protected @NonNull Member prepareActiveMember() {
        this.member = memberRegister.register(MemberFixture.createMemberRegisterRequest());
        this.member.activate();
        return this.member;
    }

    protected Course prepareCourse() {
        prepareInstructor();
        this.course = courseCreator.create(CourseFixture.createCourseCreateRequest(instructor.getId(), null));
        this.course.updateInfo(CourseFixture.createCourseInfoUpdateRequest(null).toInfo());
        return this.course;
    }

    protected Course preparePublishedCourse() {
        Course course = prepareCourse();
        course.submitForReview();
        course.publish();

        return course;
    }

    protected Enrollment prepareEnrollment(){
        Member activeMember = prepareActiveMember();
        Course publishedCourse = preparePublishedCourse();
        this.enrollment = enroller.enroll(new EnrollRequest(activeMember.getId(), publishedCourse.getId()));

        return this.enrollment;
    }
}

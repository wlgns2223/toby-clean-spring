package tobyspring.splearn.domain.cirriculumn;

import tobyspring.splearn.domain.course.CourseFixture;

public class CurriculumFixture {


    public static Curriculum createCurriculum(){
        return new Curriculum(CourseFixture.createCourse());
    }
}

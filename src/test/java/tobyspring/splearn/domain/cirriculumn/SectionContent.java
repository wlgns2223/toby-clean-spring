package tobyspring.splearn.domain.cirriculumn;

import java.util.List;

public record SectionContent(String title, List<LessonContent> lessonContents) {

    public static List<SectionContent> from(Curriculum curriculum){
        return curriculum.getSections().stream().map(section -> new SectionContent(
                section.getTitle(),
                section.getLessons().stream()
                        .map(lesson -> new LessonContent(lesson.getTitle()))
                        .toList()

        )).toList();
    }


}

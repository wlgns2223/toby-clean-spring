package tobyspring.splearn.application.course.provided;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.NonNull;

public record CourseCreateRequest(
        @NotNull Long instructorId,
        @Size(min = 2, max = 100) String title,
        @Size(max = 500) String description
) {
}

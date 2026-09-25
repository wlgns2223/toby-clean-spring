package tobyspring.splearn.application.enrollment.provided;

import jakarta.validation.constraints.NotNull;
import lombok.NonNull;

public record EnrollRequest(@NotNull Long memberId, @NotNull Long courseId) {
}

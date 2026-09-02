package tobyspring.splearn.application.enrollment.provided;

import lombok.NonNull;

public record EnrollRequest(@NonNull Long memberId, @NonNull Long courseId) {
}

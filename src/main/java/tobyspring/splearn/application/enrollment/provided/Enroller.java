package tobyspring.splearn.application.enrollment.provided;

import tobyspring.splearn.domain.enrollment.Enrollment;

public interface Enroller {
    Enrollment enroll(EnrollRequest enrollRequest);

    Enrollment startStudying(Long enrollmentId);

    Enrollment complete(Long enrollmentId);
}

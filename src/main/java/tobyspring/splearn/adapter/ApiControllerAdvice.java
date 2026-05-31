package tobyspring.splearn.adapter;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import tobyspring.splearn.domain.member.DuplicateEmailException;

import java.time.LocalDateTime;

@ControllerAdvice
public class ApiControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleException(Exception e){
        return getProblemDetail(HttpStatus.INTERNAL_SERVER_ERROR, e);
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ProblemDetail emailExceptionHandler(DuplicateEmailException e){
        // RFC 9457에 정의된 예외를 다루는 방식

        // 버그인 경우에는 그냥 예외를 던지면 되는데
        // 도메인 예외의 경우에는 커스텀 예외를 던지면 될꺼같다.


        return getProblemDetail(HttpStatus.CONFLICT,e);
    }

    @NonNull
    private static ProblemDetail getProblemDetail(HttpStatus status,  Exception e) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(status, e.getMessage());
        detail.setProperty("timestamp", LocalDateTime.now());
        detail.setProperty("exception", e.getClass().getSimpleName());
        return detail;
    }

}

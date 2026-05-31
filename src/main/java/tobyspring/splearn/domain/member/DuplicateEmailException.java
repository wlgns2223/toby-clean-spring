package tobyspring.splearn.domain.member;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// @ResponseStatus(HttpStatus.BAD_REQUEST)

/**
 * 이것은 도메인 로직에서 던지는 도메인 예외인데,
 * @ResponseStatus(HttpStatus.BAD_REQUEST) 이걸 사용하면 도메인 레이어에 웹API 계층의 로직이 들어오게됨
 * 도메인 레이어가 웹에 의존하는 모양임.
 * 따라서 도메인 예외를 던지고 상태코드 추가는 웹계층에서 해주도록 함 -> Controller Advice 사용
 */

public class DuplicateEmailException extends RuntimeException {
    public DuplicateEmailException(String message) {
        super(message);
    }
}

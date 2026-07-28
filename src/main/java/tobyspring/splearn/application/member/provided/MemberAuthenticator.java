package tobyspring.splearn.application.member.provided;

import jakarta.validation.Valid;
import tobyspring.splearn.domain.member.Member;

/*
중요한 관문이기 때문이기때문에
어떠한 인터페이스인지 코멘트 정도는 달아주자
 */
public interface MemberAuthenticator {
    Member login(@Valid MemberLoginRequest memberLoginRequest) throws LoginFailedException;

}

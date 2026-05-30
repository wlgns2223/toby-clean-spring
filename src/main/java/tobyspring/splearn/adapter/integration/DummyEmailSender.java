package tobyspring.splearn.adapter.integration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Fallback;
import org.springframework.stereotype.Component;
import tobyspring.splearn.application.member.required.EmailSender;
import tobyspring.splearn.domain.shared.Email;

@Slf4j
@Component
@Fallback // 똑같은 빈이 많을 경우, 다른 빈이 미완성이라면 이걸 사용하고 다른 빈이 있다면 그걸 사용
public class DummyEmailSender implements EmailSender {
    @Override
    public void send(Email email, String subject, String body) {
        log.info("send email...");
    }
}

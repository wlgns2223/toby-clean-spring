package tobyspring.splearn;

import org.assertj.core.api.AssertProvider;
import org.assertj.core.api.Assertions;
import org.springframework.lang.NonNull;
import org.springframework.test.json.JsonPathValueAssert;
import tobyspring.splearn.application.member.provided.MemberRegisterRequest;

import java.util.function.Consumer;

public class AssertUtils {

    @NonNull
    public static Consumer<AssertProvider<JsonPathValueAssert>> equalsTo(MemberRegisterRequest request) {
        return (email) -> Assertions.assertThat(email).isEqualTo(request.email());
    }

    @NonNull
    public static Consumer<AssertProvider<JsonPathValueAssert>> notnull() {
        return (value) -> Assertions.assertThat(value).isNotNull();
    }
}

package tobyspring.learning.instancio;

import org.instancio.Instancio;
import org.instancio.Model;
import org.instancio.Select;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

public class InstancioLearningTest {

    @Test
    @DisplayName("learning")
    void learning() {
        // given
        User user = Instancio.of(User.class)
                .ignore(Select.field(User::getId))
                .generate(Select.field(User::getEmail), (gen) -> gen.net().email())
                .set(Select.field(User::getStatus), UserStatus.PENDING)
                .create();

        assertThat(user.getId()).isNull();
        
    }
    
    @Test
    @DisplayName("model")
    void model() {
        // given
        Model<User> model = Instancio.of(User.class)
                .ignore(Select.field(User::getId))
                .generate(Select.field(User::getEmail), (gen) -> gen.net().email())
                .set(Select.field(User::getStatus), UserStatus.PENDING)
                .toModel();

        for(int i=0; i<100 ; i++){
            User user = Instancio.of(model).create();

            assertThat(user.getId()).isNull();
            assertThat(user.getEmail()).isNotNull();
        }
        
    }
    
    @Test
    @DisplayName("record")
    void record() {
        // given
        // instancio.properties 에 bean validation을 보도록 만드는 설정 추가
        UserRegisterRequest request = Instancio.of(UserRegisterRequest.class).create();
        assertThat(request.email()).isNotEmpty();

    }
}

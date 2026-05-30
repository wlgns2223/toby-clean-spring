package tobyspring.splearn.domain.member;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class ProfileTest {

    @Test
    void profile() {
        new Profile("tobyyyyyyyy");
        new Profile("1235");
    }

    @Test
    void profileFail() {
        assertThatThrownBy(() -> new Profile("")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Profile("0123456789101112131415")).isInstanceOf(IllegalArgumentException.class);

    }

    @Test
    void url() {
        Profile profile = new Profile("toby");

        assertThat(profile.url()).isEqualTo("@toby");
    }

}
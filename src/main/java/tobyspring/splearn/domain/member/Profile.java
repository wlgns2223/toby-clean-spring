package tobyspring.splearn.domain.member;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.regex.Pattern;

@Embeddable
public record Profile(@Column(name = "profile") String value) {

    private static final Pattern PROFILE_ADDRESS_PATTERN = Pattern.compile("[a-z0-9]+");

    public Profile {
        if(!PROFILE_ADDRESS_PATTERN.matcher(value).matches()){
            throw new IllegalArgumentException("프로플 주소 형식이 바르지 않습니다.");
        }

        if(value.length() > 15) throw new IllegalArgumentException("프로필 주소는 최대 15자리를 넘을 수 없습니다.");
    }

    public String url(){
        return "@" + value;
    }
}

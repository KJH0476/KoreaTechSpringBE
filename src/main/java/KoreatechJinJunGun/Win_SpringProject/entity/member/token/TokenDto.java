package KoreatechJinJunGun.Win_SpringProject.entity.member.token;

import lombok.Builder;
import lombok.Getter;

@Getter
public class TokenDto {

    String accessToken;
    String refreshToken;

    @Builder
    public TokenDto(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}

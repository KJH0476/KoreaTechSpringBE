package KoreatechJinJunGun.Win_SpringProject.entity.member.token;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@RedisHash(value = "token", timeToLive = 300)
public class RefreshToken {

    @Id
    Long id;
    String refreshToken;

    //이메일 값으로 리프레시 토큰 조회
    @Indexed
    String email;

    @Builder
    public RefreshToken(String refreshToken, String email) {
        this.refreshToken = refreshToken;
        this.email = email;
    }
}

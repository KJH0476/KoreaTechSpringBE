package KoreatechJinJunGun.Win_SpringProject.repository.member.token;

import KoreatechJinJunGun.Win_SpringProject.entity.member.token.RefreshToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TokenRepository extends CrudRepository<RefreshToken, String> {
    Optional<RefreshToken> findByEmail(String email);
}

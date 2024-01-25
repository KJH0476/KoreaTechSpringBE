package KoreatechJinJunGun.Win_SpringProject.entity.member;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class LoginForm {

    @NotNull(message = "이메일을 입력해주세요.")
    String email;
    @NotNull(message = "비밀번호를 입력해주세요.")
    String password;
}

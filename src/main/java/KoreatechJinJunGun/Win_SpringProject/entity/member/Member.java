package KoreatechJinJunGun.Win_SpringProject.entity.member;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@Entity
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;
    private String username;
    private String nickname;
    private Date createdate;
    private String birth;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Builder
    public Member(Long id, String email, String password, String username, String nickname, Date createdate, String birth, Role role) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.nickname = nickname;
        this.createdate = createdate;
        this.birth = birth;
        this.role = role;
    }
}

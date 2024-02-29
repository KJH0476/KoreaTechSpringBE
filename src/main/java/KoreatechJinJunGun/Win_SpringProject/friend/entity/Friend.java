package KoreatechJinJunGun.Win_SpringProject.friend.entity;

import KoreatechJinJunGun.Win_SpringProject.member.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Friend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", referencedColumnName = "id")
    private Member userId;
    private Long friendId;
    private String nickname;
    private Integer relationStatus;
    private Date applyTime;
    private Date relationDate;
}

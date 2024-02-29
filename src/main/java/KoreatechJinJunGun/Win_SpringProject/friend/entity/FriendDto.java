package KoreatechJinJunGun.Win_SpringProject.friend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FriendDto {

    private Long id;
    private Long friendId;
    private String nickname;
}

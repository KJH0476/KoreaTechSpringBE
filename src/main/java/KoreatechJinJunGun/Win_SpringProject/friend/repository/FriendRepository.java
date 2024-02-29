package KoreatechJinJunGun.Win_SpringProject.friend.repository;

import KoreatechJinJunGun.Win_SpringProject.friend.entity.Friend;
import KoreatechJinJunGun.Win_SpringProject.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {

    List<Friend> findByUserIdAndRelationStatus(Member userId, Integer relationStatus);

    //Containing : % 와일드카드 붙여줌 --> LIKE %nickname%
    List<Friend> findByUserIdAndNicknameContainingAndRelationStatus(Member userId, String nickname, Integer relationStatus);

    //친구 서로 삭제
    @Modifying
    void deleteByUserIdAndFriendId(Member userId, Long friendId);

    //서로 친구관계로 update
    @Modifying
    @Query("update Friend f set f.relationStatus = :relationStatus, f.relationDate = :relationDate where f.userId = :userId and f.friendId = :friendId")
    void updateRelationStatusAndRelationDate(@Param("relationStatus") Integer relationStatus, @Param("relationDate")Date relationDate,
                                             @Param("userId") Member userId, @Param("friendId") Long friendId);
}

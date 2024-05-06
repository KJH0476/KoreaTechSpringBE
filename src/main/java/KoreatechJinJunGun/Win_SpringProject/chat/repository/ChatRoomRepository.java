package KoreatechJinJunGun.Win_SpringProject.chat.repository;

import KoreatechJinJunGun.Win_SpringProject.chat.entity.chatRoom.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    //ChatRoom, ChatRoomInfo JOIN
    //@Query("select cr from ChatRoom cr left outer join fetch cr.chatRoomId cri WHERE cr.memberId.id = :memberId")
    @Query("select cr from ChatRoom cr join fetch cr.memberId m join fetch cr.chatRoomId cri where cri.id in "
            + "(select cri.chatRoomId.id from ChatRoom cri where cri.memberId.id = :memberId)")
    List<ChatRoom> findAllChatRoomsAndMembersByMemberId(@Param("memberId") Long memberId);
}

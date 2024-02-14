package KoreatechJinJunGun.Win_SpringProject.chat.repository;

import KoreatechJinJunGun.Win_SpringProject.chat.entity.chatroom.ChatRoom;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ChatRoomRepository extends MongoRepository<ChatRoom, String> {
    Optional<ChatRoom> findBySenderIdAndRecipientId(String senderId, String recipientId);
}

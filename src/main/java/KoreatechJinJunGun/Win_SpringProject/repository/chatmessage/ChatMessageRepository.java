package KoreatechJinJunGun.Win_SpringProject.repository.chatmessage;

import KoreatechJinJunGun.Win_SpringProject.entity.chatmessage.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {
    List<ChatMessage> findByChatId(String chatId);
}

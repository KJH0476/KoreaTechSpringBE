package KoreatechJinJunGun.Win_SpringProject.chat.repository;

import KoreatechJinJunGun.Win_SpringProject.chat.entity.chatuser.Status;
import KoreatechJinJunGun.Win_SpringProject.chat.entity.chatuser.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {
    List<User> findAllByStatus(Status status);
}

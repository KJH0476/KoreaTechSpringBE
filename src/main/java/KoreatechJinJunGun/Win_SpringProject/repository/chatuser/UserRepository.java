package KoreatechJinJunGun.Win_SpringProject.repository.chatuser;

import KoreatechJinJunGun.Win_SpringProject.entity.chatuser.Status;
import KoreatechJinJunGun.Win_SpringProject.entity.chatuser.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {
    List<User> findAllByStatus(Status status);
}

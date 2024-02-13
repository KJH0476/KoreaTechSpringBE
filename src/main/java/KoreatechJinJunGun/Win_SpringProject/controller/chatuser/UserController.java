package KoreatechJinJunGun.Win_SpringProject.controller.chatuser;

import KoreatechJinJunGun.Win_SpringProject.entity.chatuser.User;
import KoreatechJinJunGun.Win_SpringProject.service.chatuser.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController {

//    private final UserService userService;
//
//    @MessageMapping("/user.addUser")
//    @SendTo("/user/public")
//    public User addUser(
//            @Payload User user
//    ) {
//        userService.saveUser(user);
//        return user;
//    }
//
//    @MessageMapping("/user.disconnectUser")
//    @SendTo("/user/public")
//    public User disconnectUser(
//            @Payload User user
//    ) {
//        userService.disconnect(user);
//        return user;
//    }
//
//    @GetMapping("/user/users")
//    public ResponseEntity<List<User>> findConnectedUsers() {
//        return ResponseEntity.ok(userService.findConnectedUsers());
//    }
    private final UserService userService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/user.addUser")
    public void addUser(@Payload User user) {
        userService.saveUser(user);
        messagingTemplate.convertAndSendToUser(user.getNickName(), "/queue/messages", user);
    }

    @MessageMapping("/user.disconnectUser")
    public void disconnectUser(@Payload User user) {
        userService.disconnect(user);
        messagingTemplate.convertAndSendToUser(user.getNickName(), "/queue/messages", user);
    }

    @GetMapping("/user/users")
    public ResponseEntity<List<User>> findConnectedUsers() {
        return ResponseEntity.ok(userService.findConnectedUsers());
    }
}

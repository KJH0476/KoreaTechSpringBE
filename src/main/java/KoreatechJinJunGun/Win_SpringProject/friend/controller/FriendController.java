package KoreatechJinJunGun.Win_SpringProject.friend.controller;

import KoreatechJinJunGun.Win_SpringProject.friend.entity.Friend;
import KoreatechJinJunGun.Win_SpringProject.friend.entity.FriendDto;
import KoreatechJinJunGun.Win_SpringProject.friend.service.FriendService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FriendController {

    private final FriendService friendService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    //친구 조회
    //usrId: 현재 로그인한 사용자 Id, status: 0(친구 관계), 1(보낸 요청), 2(받은 요청)
    @GetMapping("/find-friend/{userId}/{status}")
    public ResponseEntity<List<Friend>> searchAllFriend(@PathVariable("userId") Long userId, @PathVariable("status") Integer status) throws JsonProcessingException {
        List<Friend> friends = friendService.findFriend(userId, status);

        return new ResponseEntity<>(friends, HttpStatus.OK);
    }

    //친구 검색(친구 관계 사용자 중에서 검색)
    //url 파라미터로 검색한 nickname 보냄
    @GetMapping("/search-friend/{userId}")
    public ResponseEntity<List<Friend>> searchFriend(@PathVariable("userId") Long userId, @RequestParam("nickname") String nickname) throws JsonProcessingException {
        List<Friend> friends = friendService.findSpecificFriend(userId, nickname, 0);

        return new ResponseEntity<>(friends, HttpStatus.OK);
    }

    //친구 요청
    @PostMapping("/request-friend")
    public ResponseEntity<Map<String, String>> addFriend(@RequestBody FriendDto friendDto){
        String friendEmail = friendService.requestFriend(friendDto);

        //웹소켓 실시간 요청 알림을 친구에게 보냄
        sendFriendRequestNotification(friendEmail, "새로운 친구 요청");
        log.info("친구 요청 완료 {} -> {}", friendDto.getId(), friendDto.getFriendId());

        return new ResponseEntity<>(getResponseBody("새로운 친구 요청"), HttpStatus.OK);
    }

    //친구 수락
    @PostMapping("/received-friend")
    public ResponseEntity<Map<String, String>> okFriend(@RequestBody FriendDto friendDto){
        friendService.receivedFriend(friendDto.getId(), friendDto.getFriendId());

        return new ResponseEntity<>(getResponseBody("친구 요청을 수락하였습니다."), HttpStatus.OK);
    }

    //친구 삭제, 거절
    @PostMapping("/delete-friend")
    public ResponseEntity<Map<String, String>> removeFriend(@RequestBody FriendDto friendDto){
        friendService.removeEachFriend(friendDto.getId(), friendDto.getFriendId());

        return new ResponseEntity<>(getResponseBody("친구를 삭제하였습니다."), HttpStatus.OK);
    }

    //친구 요청 사용자를 찾을 수 없는 경우 -> 404
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map<String, String>> handleNoSuchElementException(NoSuchElementException ex) {

        return new ResponseEntity<>(getResponseBody(ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    public void sendFriendRequestNotification(String friendEmail, String sendMessage){
        //특정 사용자에게 메시지를 보내려 할 때 convertAndSendToUser 메서드의 첫 번째 인자는 SpringSecurity 가 관리하는 사용자 식별자와 일치해야 함
        //현재 SpringSecurity 가 관리하는 식별자는 Email
        simpMessagingTemplate.convertAndSendToUser(friendEmail, "/queue/friendNotification", sendMessage);
    }

    private Map<String, String> getResponseBody(String message){
        Map<String, String> body = new ConcurrentHashMap<>();
        body.put("message", message);
        return body;
    }
}

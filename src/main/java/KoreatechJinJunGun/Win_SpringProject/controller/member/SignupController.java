package KoreatechJinJunGun.Win_SpringProject.controller.member;

import KoreatechJinJunGun.Win_SpringProject.entity.member.Member;
import KoreatechJinJunGun.Win_SpringProject.entity.member.Role;
import KoreatechJinJunGun.Win_SpringProject.entity.member.SignUpForm;
import KoreatechJinJunGun.Win_SpringProject.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SignupController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<Map<String, String>> singUp(@Validated @RequestBody SignUpForm signUpForm,
                                         BindingResult bindingResult) {
        Map<String, String> message = new ConcurrentHashMap<>();
        //예외 처리
        if(bindingResult.hasErrors()){
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                message.put(error.getField(), error.getDefaultMessage());
            }
            //400 반환
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }

        //회원가입 성공
        LocalDateTime createTime = LocalDateTime.now();

        Member member = memberService.addMember(Member.builder()
                .email(signUpForm.getEmail())
                .password(signUpForm.getPassword())
                .username(signUpForm.getUsername())
                .nickname((signUpForm.getNickname().isEmpty()) ? signUpForm.getUsername() : signUpForm.getNickname())
                .createdate(Timestamp.valueOf(createTime))
                .birth(signUpForm.getBirth())
                .role(Role.USER)
                .build());
        message.put("message", "Success Signup");
        //성공 200 반환
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    //사용자 명 겹치는지 확인
    @GetMapping("/checkUsername")
    public ResponseEntity<Map<String, String>> confirmUsername(@RequestParam("username") String username){
        Map<String, String> message = new ConcurrentHashMap<>();

        //이미 사용자명이 존재하면 409 반환
        if(!memberService.checkUsername(username)) {
            message.put("message", "이미 존재하는 사용자 명입니다.");
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        }

        message.put("message", "사용할 수 있는 사용자 명입니다."); 
        //존재하지 않으면 200 반환
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}

package KoreatechJinJunGun.Win_SpringProject.service.member;

import KoreatechJinJunGun.Win_SpringProject.entity.member.Member;
import KoreatechJinJunGun.Win_SpringProject.entity.member.Role;
import KoreatechJinJunGun.Win_SpringProject.entity.member.SignUpForm;
import KoreatechJinJunGun.Win_SpringProject.repository.member.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Member signupMember(SignUpForm signUpForm){
        LocalDateTime createTime = LocalDateTime.now();

        //새로운 회원 생성 후 저장
        Member member = Member.builder()
                .email(signUpForm.getEmail())
                .password(passwordEncoder.encode(signUpForm.getPassword()))
                .username(signUpForm.getUsername())
                .nickname((signUpForm.getNickname().isEmpty()) ? signUpForm.getUsername() : signUpForm.getNickname())
                .createdate(Timestamp.valueOf(createTime))
                .birth(signUpForm.getBirth())
                .role(Role.USER)
                .build();
        return memberRepository.save(member);
    }

    public Boolean checkValue(String check){
        //체크할 값이 이메일일 경우
        if(check.contains("@")){
            //같은 이메일이 존재하지 않을 경우 true 리턴, 존재 할 경우 false 리턴
            return memberRepository.findByEmail(check).isEmpty();
        }
        //체크할 값이 사용자 명일 경우
        //같은 사용자 명이 존재하지 않으면 true 리턴, 존재할 경우 false 리턴
        return memberRepository.findByUsername(check).isEmpty();
    }
}

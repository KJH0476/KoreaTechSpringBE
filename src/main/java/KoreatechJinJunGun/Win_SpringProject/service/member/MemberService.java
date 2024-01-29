package KoreatechJinJunGun.Win_SpringProject.service.member;

import KoreatechJinJunGun.Win_SpringProject.entity.member.Member;
import KoreatechJinJunGun.Win_SpringProject.repository.member.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Member addMember(Member member){
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

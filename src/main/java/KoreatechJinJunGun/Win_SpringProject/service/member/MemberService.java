package KoreatechJinJunGun.Win_SpringProject.service.member;

import KoreatechJinJunGun.Win_SpringProject.entity.member.Member;
import KoreatechJinJunGun.Win_SpringProject.repository.member.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Member addMember(Member member){
        return memberRepository.save(member);
    }

    public boolean checkUsername(String username){
        //같은 사용자 명이 존재하면 false 리턴, 존재하지 않으면 true 리턴
        return memberRepository.findByUsername(username) == null;

    }
}

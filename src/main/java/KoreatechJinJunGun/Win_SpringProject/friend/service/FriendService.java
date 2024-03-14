package KoreatechJinJunGun.Win_SpringProject.friend.service;

import KoreatechJinJunGun.Win_SpringProject.friend.entity.FriendDto;
import KoreatechJinJunGun.Win_SpringProject.friend.repository.FriendRepository;
import KoreatechJinJunGun.Win_SpringProject.friend.entity.Friend;
import KoreatechJinJunGun.Win_SpringProject.member.entity.Member;
import KoreatechJinJunGun.Win_SpringProject.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class FriendService {

    private final FriendRepository friendRepository;
    private final MemberRepository memberRepository;

    //전체 친구, 받은 요청, 보낸 요청 조회
    public List<Friend> findFriend(Long userId, Integer relationStatus){
        return memberRepository.findById(userId)
                .map(member -> friendRepository.findByUserIdAndRelationStatus(member, relationStatus))
                .orElseGet(Collections::emptyList); // 불변한 빈 리스트 반환
    }


    //현재 친구 nickname 으로 검색
    public List<Friend> findSpecificFriend(Long userId, String nickname, Integer relationStatus){
        return memberRepository.findById(userId)
                .map(member -> friendRepository.findByUserIdAndNicknameContainingAndRelationStatus(member, nickname, relationStatus))
                .orElse(Collections.emptyList());
    }

    public String requestFriend(FriendDto friendDto) {
        Member member = memberRepository.findById(friendDto.getId())
                .orElseThrow(() -> new NoSuchElementException("요청한 사용자를 찾을 수 없습니다."));

        Member friendMember = memberRepository.findById(friendDto.getFriendId())
                .orElseThrow(() -> new NoSuchElementException("친구 사용자를 찾을 수 없습니다."));

        createAndSaveFriend(friendMember, friendDto.getId(), member.getNickname(), 2);
        createAndSaveFriend(member, friendDto.getFriendId(), friendDto.getNickname(), 1);

        //알림을 위해 요청을 받을 친구의 이메일 반환
        return friendMember.getEmail();
    }

    private void createAndSaveFriend(Member user, Long friendId, String nickname, Integer relationStatus) {
        Friend friend = Friend.builder()
                .userId(user)
                .friendId(friendId)
                .nickname(nickname)
                .relationStatus(relationStatus)
                .applyTime(new Date(System.currentTimeMillis()))
                .build();
        friendRepository.save(friend);
    }

    public void receivedFriend(Long userId, Long friendId){
        Date date = new Date(System.currentTimeMillis());
        Member member = memberRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다."));
        Member friendMember = memberRepository.findById(friendId).orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다."));
        friendRepository.updateRelationStatusAndRelationDate(0, date, member, friendId);
        friendRepository.updateRelationStatusAndRelationDate(0, date, friendMember, userId);
    }

    public void removeEachFriend(Long userId, Long friendId){
        Member member = memberRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다."));
        Member friendMember = memberRepository.findById(friendId).orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다."));
        friendRepository.deleteByUserIdAndFriendId(member, friendId);
        friendRepository.deleteByUserIdAndFriendId(friendMember, userId);
    }
}

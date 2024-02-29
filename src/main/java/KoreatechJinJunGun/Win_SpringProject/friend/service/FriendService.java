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
        Optional<Member> memberOp = memberRepository.findById(userId);
        if(memberOp.isPresent()) {
            return friendRepository.findByUserIdAndRelationStatus(memberOp.get(), relationStatus);
        }
        //불변한 빈 리스트 반환(리스트 변경불가) -> 추가 메모리 할당을 피할 수 있음
        return Collections.emptyList();
    }

    //현재 친구 nickname 으로 검색
    public List<Friend> findSpecificFriend(Long userId, String nickname, Integer relationStatus){
        Optional<Member> memberOp = memberRepository.findById(userId);
        if(memberOp.isPresent()) {
            return friendRepository.findByUserIdAndNicknameContainingAndRelationStatus(memberOp.get(), nickname, relationStatus);
        }
        return Collections.emptyList();
    }

    public void requestFriend(FriendDto friendDto) {
        Member member = memberRepository.findById(friendDto.getId())
                .orElseThrow(() -> new NoSuchElementException("요청한 사용자를 찾을 수 없습니다."));

        Member friendMember = memberRepository.findById(friendDto.getFriendId())
                .orElseThrow(() -> new NoSuchElementException("친구 사용자를 찾을 수 없습니다."));

        createAndSaveFriend(friendMember, friendDto.getId(), member.getNickname(), 2);
        createAndSaveFriend(member, friendDto.getFriendId(), friendDto.getNickname(), 1);
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

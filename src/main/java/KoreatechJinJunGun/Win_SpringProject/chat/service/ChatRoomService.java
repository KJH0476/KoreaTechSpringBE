package KoreatechJinJunGun.Win_SpringProject.chat.service;

import KoreatechJinJunGun.Win_SpringProject.chat.entity.chatRoom.ChatRoom;
import KoreatechJinJunGun.Win_SpringProject.chat.entity.chatRoom.ChatRoomInfo;
import KoreatechJinJunGun.Win_SpringProject.chat.entity.chatRoom.dto.ChatRoomDto;
import KoreatechJinJunGun.Win_SpringProject.chat.entity.chatRoom.dto.ChatRoomInfoDto;
import KoreatechJinJunGun.Win_SpringProject.chat.entity.chatRoom.dto.ChatRoomMemberDto;
import KoreatechJinJunGun.Win_SpringProject.member.entity.Member;
import KoreatechJinJunGun.Win_SpringProject.member.repository.MemberRepository;
import KoreatechJinJunGun.Win_SpringProject.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;

    public List<ChatRoom> addChatRoom(ChatRoomInfo chatRoomInfo, ChatRoomDto chatRoomDto){

        return chatRoomRepository.saveAll(Arrays.asList(
                ChatRoom.builder()
                        .memberId(findParticipant(chatRoomDto.getFirstParticipantId()))
                        .chatRoomId(chatRoomInfo)
                        .build(),
                ChatRoom.builder()
                        .memberId(findParticipant(chatRoomDto.getSecondParticipantId()))
                        .chatRoomId(chatRoomInfo)
                        .build()
                )
        );
    }

    public List<ChatRoomInfoDto> findChatRoomInfoByChatRoom(Long memberId){
        List<ChatRoom> chatRooms = chatRoomRepository.findAllChatRoomsAndMembersByMemberId(memberId);

        Map<ChatRoomInfo, List<ChatRoomMemberDto>> chatRoomInfoMap = chatRooms.stream()
                .filter(chatRoom -> !chatRoom.getMemberId().getId().equals(memberId))   //자기 자신을 제외함
                .collect(Collectors.groupingBy(
                        ChatRoom::getChatRoomId,
                        Collectors.mapping(
                                chatRoom -> new ChatRoomMemberDto(
                                        chatRoom.getMemberId().getEmail(),
                                        chatRoom.getMemberId().getUsername(),
                                        chatRoom.getMemberId().getNickname()),
                                Collectors.toList()
                        )
                ));

        return chatRoomInfoMap.entrySet().stream()
                .map(entry -> ChatRoomInfoDto.builder()
                        .chatRoomInfo(entry.getKey())
                        .memberList(entry.getValue())
                        .build())
                .collect(Collectors.toList());
    }

    private Member findParticipant(Long id){
        return memberRepository
                .findById(id)
                .orElse(null);
    }
}

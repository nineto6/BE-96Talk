package nineto6.Talk.domain.chatroom.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nineto6.Talk.domain.chatroom.chatroommember.domain.ChatroomMember;
import nineto6.Talk.domain.chatroom.chatroommember.repository.ChatroomMemberRepository;
import nineto6.Talk.domain.chatroom.domain.Chatroom;
import nineto6.Talk.domain.chatroom.dto.ChatroomDto;
import nineto6.Talk.domain.chatroom.repository.ChatroomRepository;
import nineto6.Talk.domain.member.domain.Member;
import nineto6.Talk.domain.member.dto.MemberDto;
import nineto6.Talk.domain.member.repository.MemberRepository;
import nineto6.Talk.domain.profile.service.ProfileService;
import nineto6.Talk.global.chat.mongodb.service.ChatService;
import nineto6.Talk.global.error.exception.BusinessExceptionHandler;
import nineto6.Talk.global.error.exception.code.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatroomService {
    private final ChatroomRepository chatroomRepository;
    private final ChatroomMemberRepository chatroomMemberRepository;
    private final ProfileService profileService;
    private final MemberRepository memberRepository;
    private final ChatService chatService;

    /**
     * 채팅방 생성
     */
    @Transactional
    public String create(MemberDto memberDto, String friendNickname) {
        // 자기 자신과 채팅방을 만들 경우 Exception
        if(memberDto.getMemberNickname().equals(friendNickname)) {
            throw new BusinessExceptionHandler(ErrorCode.BAD_REQUEST_ERROR);
        }

        // 친구가 멤버에 존재하지 않을 경우 Exception
        Member friend = memberRepository.findByMemberNickname(friendNickname)
                .orElseThrow(() -> new BusinessExceptionHandler(ErrorCode.BAD_REQUEST_ERROR));

        // 자신과 친구의 단일 채팅방이 있는지 확인
        Optional<Chatroom> chatroomOptional = chatroomRepository.findChatroomByMemberIdAndFriendId(memberDto.getMemberId(), friend.getMemberId());

        // 단일 채팅방이 이미 있을 경우 Exception
        if(chatroomOptional.isPresent()) {
            throw new BusinessExceptionHandler(ErrorCode.ALREADY_EXISTS);
        }

        // 없을 경우 생성
        Chatroom chatroom = Chatroom.builder()
                .chatroomChannelId(UUID.randomUUID().toString())
                .chatroomWriterNickname(memberDto.getMemberNickname())
                .chatroomIsGroup(false)
                .build();

        chatroomRepository.save(chatroom);

        // 채팅방에 자신 등록
        ChatroomMember chatroomMember = ChatroomMember.builder()
                .chatroomId(chatroom.getChatroomId())
                .memberId(memberDto.getMemberId())
                .build();

        chatroomMemberRepository.save(chatroomMember);

        // 채팅방에 친구 등록
        ChatroomMember chatroomFriend = ChatroomMember.builder()
                .chatroomId(chatroom.getChatroomId())
                .memberId(friend.getMemberId())
                .build();

        chatroomMemberRepository.save(chatroomFriend);
        return chatroom.getChatroomChannelId();
    }

    /**
     * 채팅방 삭제
     */
    @Transactional
    public void deleteChatroomByChannelId(MemberDto memberDto, String channelId) {
        // 채널 아이디로 된 채팅방이 존재하지 않을 경우 Exception
        Chatroom chatroom = chatroomRepository.findByChannelId(channelId)
                .orElseThrow(() -> new BusinessExceptionHandler(ErrorCode.BAD_REQUEST_ERROR));

        // 자신이 소속된 채팅방이 아닐 경우 Exception
        chatroomMemberRepository.findByChatroomIdAndMemberId(chatroom.getChatroomId(), memberDto.getMemberId())
                .orElseThrow(() -> new BusinessExceptionHandler(ErrorCode.BAD_REQUEST_ERROR));

        // 채팅방 삭제, CASCADE 속성으로 CHATROOM_MEMBER 테이블 연관관계 삭제
        chatroomRepository.deleteByChannelId(channelId);
    }

    /**
     * 채팅방 목록 가져오기
     */
    @Transactional(readOnly = true)
    public List<ChatroomDto> getChatroomListByMemberDto(MemberDto memberDto) {
        return chatroomRepository.findChannelIdAndMemberProfileListByMemberId(memberDto.getMemberId()).stream()
                .map((chatroomProfile) ->
                        ChatroomDto.builder()
                                .chatroomChannelId(chatroomProfile.getChatroomChannelId())
                                .recentChat(chatService.findOneByChannelId(chatroomProfile.getChatroomChannelId()))
                                .profileResponseList(chatroomProfile.getMemberProfileList().stream()
                                        .map(profileService::getProfileResponse)
                                        .collect(Collectors.toList()))
                                .build()
                ).collect(Collectors.toList());
    }

    /**
     * 자신이 속한 채팅방인지 확인
     */
    public boolean isMyChatroom(String channelId, Long memberId) {
        Optional<Chatroom> chatroom = chatroomRepository.findChatroomByChannelIdAndMemberId(channelId, memberId);
        return chatroom.isPresent();
    }
}

package nineto6.Talk.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nineto6.Talk.vo.ChatroomMemberVo;
import nineto6.Talk.repository.chatroommember.ChatroomMemberRepository;
import nineto6.Talk.vo.ChatroomVo;
import nineto6.Talk.dto.chatroom.ChatroomDto;
import nineto6.Talk.dto.chatroommember.ChatroomMemberDto;
import nineto6.Talk.controller.chatroom.controller.response.ChatroomSaveResponse;
import nineto6.Talk.repository.chatroom.ChatroomRepository;
import nineto6.Talk.vo.MemberVo;
import nineto6.Talk.dto.member.MemberDto;
import nineto6.Talk.repository.member.MemberRepository;
import nineto6.Talk.global.common.code.SuccessCode;
import nineto6.Talk.global.error.exception.BusinessExceptionHandler;
import nineto6.Talk.global.error.exception.code.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
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
    public ChatroomSaveResponse create(MemberDto memberDto, String friendNickname) {
        // 자기 자신과 채팅방을 만들 경우 Exception
        if(memberDto.getMemberNickname().equals(friendNickname)) {
            throw new BusinessExceptionHandler(ErrorCode.BAD_REQUEST_ERROR);
        }

        // 친구가 멤버에 존재하지 않을 경우 Exception
        MemberVo friend = memberRepository.findByMemberNickname(friendNickname)
                .orElseThrow(() -> new BusinessExceptionHandler(ErrorCode.BAD_REQUEST_ERROR));

        // 자신과 친구의 단일 채팅방이 있는지 확인
        Optional<ChatroomVo> chatroomOptional = chatroomRepository.findChatroomByMemberIdAndFriendId(memberDto.getMemberId(), friend.getMemberId());

        // 단일 채팅방이 이미 있을 경우 Exception
        if(chatroomOptional.isPresent()) {
            // 이미 존재하는 채널 아이디 값을 반환
            return ChatroomSaveResponse.builder()
                    .chatroomChannelId(chatroomOptional.get().getChatroomChannelId())
                    .successCode(SuccessCode.SELECT_SUCCESS)
                    .build();
        }

        // 없을 경우 생성
        ChatroomVo chatroomVo = ChatroomVo.builder()
                .chatroomChannelId(UUID.randomUUID().toString())
                .chatroomWriterNickname(memberDto.getMemberNickname())
                .chatroomIsGroup(false)
                .build();

        chatroomRepository.save(chatroomVo);

        // 채팅방에 자신 등록
        ChatroomMemberVo chatroomMemberVo = ChatroomMemberVo.builder()
                .chatroomId(chatroomVo.getChatroomId())
                .memberId(memberDto.getMemberId())
                .chatroomUnSubDate(LocalDateTime.now())
                .build();

        chatroomMemberRepository.save(chatroomMemberVo);

        // 채팅방에 친구 등록
        ChatroomMemberVo chatroomFriend = ChatroomMemberVo.builder()
                .chatroomId(chatroomVo.getChatroomId())
                .memberId(friend.getMemberId())
                .chatroomUnSubDate(LocalDateTime.now())
                .build();

        chatroomMemberRepository.save(chatroomFriend);
        return ChatroomSaveResponse.builder()
                .chatroomChannelId(chatroomVo.getChatroomChannelId())
                .successCode(SuccessCode.INSERT_SUCCESS)
                .build();
    }

    /**
     * 채팅방 삭제
     */
    @Transactional
    public void deleteChatroomByChannelId(MemberDto memberDto, String channelId) {
        // 채널 아이디로 된 채팅방이 존재하지 않을 경우 Exception
        ChatroomVo chatroomVo = chatroomRepository.findByChannelId(channelId)
                .orElseThrow(() -> new BusinessExceptionHandler(ErrorCode.BAD_REQUEST_ERROR));

        // 자신이 소속된 채팅방이 아닐 경우 Exception
        chatroomMemberRepository.findByChatroomIdAndMemberId(chatroomVo.getChatroomId(), memberDto.getMemberId())
                .orElseThrow(() -> new BusinessExceptionHandler(ErrorCode.BAD_REQUEST_ERROR));

        // 채팅방 삭제, CASCADE 속성으로 CHATROOM_MEMBER 테이블 연관관계 삭제
        chatroomRepository.deleteByChannelId(channelId);
    }

    /**
     * 채팅방 멤버 채팅 구독일에 최근일 등록
     */
    @Transactional
    public void updateChatroomSubDate(String channelId, Long memberId) {
        chatroomMemberRepository.updateSubDateByChannelIdAndMemberId(channelId, memberId);
    }

    /**
     * 채팅방 멤버 채팅 구독일 삭제
     */
    @Transactional
    public void deleteChatroomSubDate(String channelId, Long memberId) {
        chatroomMemberRepository.removeSubDateByChannelIdAndMemberId(channelId, memberId);
    }

    /**
     * 채팅방 멤버 채팅 구독 취소일 등록
     */
    @Transactional
    public void updateChatroomUnSubDate(String channelId, Long memberId) {
        chatroomMemberRepository.updateUnSubDateByChannelIdAndMemberId(channelId, memberId);
    }

    /**
     * 채팅방 목록 가져오기
     */
    public List<ChatroomDto> findChatroomListByMemberDto(MemberDto memberDto) {
        return chatroomRepository.findChannelIdAndMemberProfileListByMemberId(memberDto.getMemberId()).stream()
                .map((chatroomProfile) ->
                        ChatroomDto.builder()
                                .chatroomChannelId(chatroomProfile.getChatroomChannelId())
                                .recentChat(chatService.findOneByChannelId(chatroomProfile.getChatroomChannelId()))
                                .profileResponseList(chatroomProfile.getMemberProfileList().stream()
                                        .map(profileService::getProfileResponse)
                                        .collect(Collectors.toList()))
                                .build()
                ).sorted(this::compare)
                .collect(Collectors.toList());
    }

    /**
     * 최근 메세지의 시간 순으로 오름차순 정렬, NULL 값일 경우 마지막
     */
    private int compare(ChatroomDto o1, ChatroomDto o2) {
        // o1 의 최근 메세지가 NULL 이면 o1 이 오른쪽 자리로 이동
        if (ObjectUtils.isEmpty(o1.getRecentChat())) {
            return 1;
        }

        // o2 의 최근 메세지가 NULL 이면 o1 이 왼쪽 자리로 이동
        if(ObjectUtils.isEmpty(o2.getRecentChat())) {
            return -1;
        }

        // o1 이 o2 보다 과거이면 o1 이 오른쪽 자리로 이동
        if (o1.getRecentChat().getRegdate().isBefore(o2.getRecentChat().getRegdate())) {
            return 1;
        }

        // o1 이 왼쪽으로 이동 (가장 최근 시간)
        return -1;
    }

    /**
     * 자신이 속한 채팅방인지 확인
     */
    public boolean isMyChatroom(String channelId, Long memberId) {
        Optional<ChatroomVo> chatroom = chatroomRepository.findChatroomByChannelIdAndMemberId(channelId, memberId);
        return chatroom.isPresent();
    }

    /**
     * 자신이 속한 채팅방인지 확인 후 반환
     */
    public ChatroomMemberDto findMyChatroomMemberDto(String channelId, MemberDto memberDto) {
        ChatroomMemberVo myChatroomMemberVo = chatroomMemberRepository.findByChannelIdAndMemberId(channelId, memberDto.getMemberId())
                .orElseThrow(() -> new BusinessExceptionHandler(ErrorCode.BAD_REQUEST_ERROR));

        return ChatroomMemberDto.builder()
                .channelId(channelId)
                .chatroomUnSubDate(myChatroomMemberVo.getChatroomUnSubDate())
                .chatroomSubDate(myChatroomMemberVo.getChatroomUnSubDate())
                .memberNickname(memberDto.getMemberNickname())
                .build();
    }

    /**
     * 채팅방에 소속된 인원 중에 친구가 아닌 사용자 닉네임 조회
     */
    public List<String> findNotFriendInChatroom(String channelId, MemberDto memberDto) {
        return chatroomRepository.findNotFriendInChatroomByChannelIdAndMemberId(channelId, memberDto.getMemberId());
    }

    /**
     * 채팅방 멤버 channelId 값으로 조회
     */
    public List<ChatroomMemberDto> findChatroomMemberDtoByChannelIdAndNickname(String channelId, String nickname) {
        return chatroomMemberRepository.findOtherChatroomMemberDtoByChannelIdAndNickname(channelId, nickname);
    }

    /**
     * 채팅방 멤버로 chatroomMemberDto 조회
     */
    public List<ChatroomMemberDto> findChatroomMemberDtoByMemberId(MemberDto memberDto) {
        return chatroomMemberRepository.findChatroomMemberDtoByMemberId(memberDto.getMemberId());
    }

}

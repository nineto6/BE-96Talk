package nineto6.Talk.domain.chatroom.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nineto6.Talk.domain.chatroom.controller.swagger.ChatroomControllerDocs;
import nineto6.Talk.domain.chatroom.dto.*;
import nineto6.Talk.domain.chatroom.service.ChatroomService;
import nineto6.Talk.domain.member.dto.MemberDetailsDto;
import nineto6.Talk.domain.chat.mongodb.dto.ChatResponse;
import nineto6.Talk.domain.chat.mongodb.service.ChatService;
import nineto6.Talk.global.common.code.SuccessCode;
import nineto6.Talk.global.common.response.ApiResponse;
import nineto6.Talk.global.error.exception.BusinessExceptionHandler;
import nineto6.Talk.global.error.exception.code.ErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/chatroom")
public class ChatroomController implements ChatroomControllerDocs {
    private final ChatroomService chatroomService;
    private final ChatService chatService;

    // 채팅방 생성
    @PostMapping
    public ResponseEntity<ApiResponse> createChatroom(@RequestBody ChatroomSaveRequest chatroomSaveRequest,
                                                      @AuthenticationPrincipal MemberDetailsDto memberDetailsDto) {
        ChatroomSaveDto chatroomSaveDto = chatroomService.create(memberDetailsDto.getMemberDto(), chatroomSaveRequest.getFriendNickname());

        ApiResponse apiResponse = ApiResponse.builder()
                .result(chatroomSaveDto.getChatroomChannelId())
                .status(chatroomSaveDto.getSuccessCode().getStatus())
                .message(chatroomSaveDto.getSuccessCode().getMessage())
                .build();
        return new ResponseEntity<ApiResponse>(apiResponse, chatroomSaveDto.getSuccessCode().getHttpStatus());
    }

    // 채팅방 삭제
    @DeleteMapping
    public ResponseEntity<ApiResponse> deleteChatroom(@RequestBody ChatroomDeleteRequest chatroomDeleteRequest,
                                                      @AuthenticationPrincipal MemberDetailsDto memberDetailsDto) {
        chatroomService.deleteChatroomByChannelId(memberDetailsDto.getMemberDto(), chatroomDeleteRequest.getChannelId());

        ApiResponse apiResponse = ApiResponse.builder()
                .result(null)
                .status(SuccessCode.DELETE_SUCCESS.getStatus())
                .message(SuccessCode.DELETE_SUCCESS.getMessage())
                .build();
        return new ResponseEntity<ApiResponse>(apiResponse, SuccessCode.DELETE_SUCCESS.getHttpStatus());
    }

    // 채팅방 목록 가져오기
    @GetMapping
    public ResponseEntity<ApiResponse> getChatroomList(@AuthenticationPrincipal MemberDetailsDto memberDetailsDto) {
        List<ChatroomDto> chatroomList = chatroomService.getChatroomListByMemberDto(memberDetailsDto.getMemberDto());

        ApiResponse apiResponse = ApiResponse.builder()
                .result(chatroomList)
                .status(SuccessCode.SELECT_SUCCESS.getStatus())
                .message(SuccessCode.SELECT_SUCCESS.getMessage())
                .build();
        return new ResponseEntity<ApiResponse>(apiResponse, SuccessCode.SELECT_SUCCESS.getHttpStatus());
    }

    /**
     * 채팅방 내에 채팅 메세지 조회
     */
    @GetMapping("/{channelId}/messages")
    public ResponseEntity<ApiResponse> getChatLog(@PathVariable("channelId") String channelId,
                                                  @AuthenticationPrincipal MemberDetailsDto memberDetailsDto) {
        // 자신이 소속된 채팅방인지 확인
        if(!chatroomService.isMyChatroom(channelId, memberDetailsDto.getMemberDto().getMemberId())) {
            throw new BusinessExceptionHandler(ErrorCode.BAD_REQUEST_ERROR);
        }

        List<ChatResponse> chatResponseList = chatService.findChatByChannelId(channelId);

        ApiResponse apiResponse = ApiResponse.builder()
                .result(chatResponseList)
                .status(SuccessCode.SELECT_SUCCESS.getStatus())
                .message(SuccessCode.SELECT_SUCCESS.getMessage())
                .build();
        return new ResponseEntity<ApiResponse>(apiResponse, SuccessCode.SELECT_SUCCESS.getHttpStatus());
    }

    /**
     * 채팅방에 소속된 인원 중에 친구가 아닌 사용자 닉네임 조회
     */
    @GetMapping("/{channelId}")
    public ResponseEntity<ApiResponse> getNotFriendNicknameListInChatroom(@PathVariable("channelId") String channelId,
                                                                          @AuthenticationPrincipal MemberDetailsDto memberDetailsDto) {
        // 자신이 소속된 채팅방인지 확인
        if(!chatroomService.isMyChatroom(channelId, memberDetailsDto.getMemberDto().getMemberId())) {
            throw new BusinessExceptionHandler(ErrorCode.BAD_REQUEST_ERROR);
        }

        List<String> nicknameList = chatroomService.findNotFriendInChatroom(channelId, memberDetailsDto.getMemberDto());

        ApiResponse apiResponse = ApiResponse.builder()
                .result(nicknameList)
                .status(SuccessCode.SELECT_SUCCESS.getStatus())
                .message(SuccessCode.SELECT_SUCCESS.getMessage())
                .build();
        return new ResponseEntity<ApiResponse>(apiResponse, SuccessCode.SELECT_SUCCESS.getHttpStatus());
    }

    /**
     * 모든 채팅방 알람 데이터 전체 개수 조회
     */
    @GetMapping("/alerts")
    public ResponseEntity<ApiResponse> getAllAlertMessage(@AuthenticationPrincipal MemberDetailsDto memberDetailsDto) {
        List<ChatroomMemberDto> chatroomMemberDtoList = chatroomService.findChatroomMemberDtoByMemberId(memberDetailsDto.getMemberDto());
        Long alertTotalCount = chatService.findAlertTotalCountByChatroomMemberDtoList(chatroomMemberDtoList);

        ApiResponse apiResponse = ApiResponse.builder()
                .result(alertTotalCount)
                .status(SuccessCode.SELECT_SUCCESS.getStatus())
                .message(SuccessCode.SELECT_SUCCESS.getMessage())
                .build();
        return new ResponseEntity<ApiResponse>(apiResponse, SuccessCode.SELECT_SUCCESS.getHttpStatus());
    }

    /**
     * channelId 값으로 채팅방 알람 데이터 개수 조회
     */
    @GetMapping("/{channelId}/alerts")
    public ResponseEntity<ApiResponse> getChatroomAlertMessage(@PathVariable("channelId") String channelId,
                                                               @AuthenticationPrincipal MemberDetailsDto memberDetailsDto) {
        ChatroomMemberDto chatroomMemberDto = chatroomService.getMyChatroomMemberDto(channelId, memberDetailsDto.getMemberDto());
        Long alertCount = chatService.findAlertCountByChatroomMemberDto(chatroomMemberDto);

        ApiResponse apiResponse = ApiResponse.builder()
                .result(alertCount)
                .status(SuccessCode.SELECT_SUCCESS.getStatus())
                .message(SuccessCode.SELECT_SUCCESS.getMessage())
                .build();
        return new ResponseEntity<ApiResponse>(apiResponse, SuccessCode.SELECT_SUCCESS.getHttpStatus());
    }
}

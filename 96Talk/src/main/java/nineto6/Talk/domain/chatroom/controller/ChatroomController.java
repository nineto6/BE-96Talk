package nineto6.Talk.domain.chatroom.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nineto6.Talk.domain.chatroom.controller.swagger.ChatroomControllerDocs;
import nineto6.Talk.domain.chatroom.dto.ChatroomDeleteRequest;
import nineto6.Talk.domain.chatroom.dto.ChatroomDto;
import nineto6.Talk.domain.chatroom.dto.ChatroomSaveRequest;
import nineto6.Talk.domain.chatroom.service.ChatroomService;
import nineto6.Talk.domain.member.dto.MemberDetailsDto;
import nineto6.Talk.global.chat.mongodb.dto.ChatResponse;
import nineto6.Talk.global.chat.mongodb.service.ChatService;
import nineto6.Talk.global.common.code.SuccessCode;
import nineto6.Talk.global.common.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        String channelId = chatroomService.create(memberDetailsDto.getMemberDto(), chatroomSaveRequest.getFriendNickname());
        ApiResponse apiResponse = ApiResponse.builder()
                .result(channelId)
                .status(SuccessCode.INSERT_SUCCESS.getStatus())
                .message(SuccessCode.INSERT_SUCCESS.getMessage())
                .build();
        return new ResponseEntity<ApiResponse>(apiResponse, SuccessCode.INSERT_SUCCESS.getHttpStatus());
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

    @GetMapping("/chat")
    public ResponseEntity<ApiResponse> getChatLog(@RequestParam("channelId") String channelId) {

        List<ChatResponse> chatResponseList = chatService.findChatByChannelId(channelId);

        ApiResponse apiResponse = ApiResponse.builder()
                .result(chatResponseList)
                .status(SuccessCode.SELECT_SUCCESS.getStatus())
                .message(SuccessCode.SELECT_SUCCESS.getMessage())
                .build();
        return new ResponseEntity<ApiResponse>(apiResponse, SuccessCode.SELECT_SUCCESS.getHttpStatus());
    }
}

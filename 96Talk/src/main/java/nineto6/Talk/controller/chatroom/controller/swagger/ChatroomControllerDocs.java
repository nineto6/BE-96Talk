package nineto6.Talk.controller.chatroom.controller.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import nineto6.Talk.controller.chatroom.controller.request.ChatroomDeleteRequest;
import nineto6.Talk.controller.chatroom.controller.request.ChatroomSaveRequest;
import nineto6.Talk.dto.member.MemberDetailsDto;
import nineto6.Talk.global.common.response.ApiResponse;
import org.springframework.http.ResponseEntity;


@Tag(name = "채팅방")
public interface ChatroomControllerDocs {
    @Operation(summary = "채팅방 리스트 조회", description = "토큰이 필요합니다.")
    ResponseEntity<ApiResponse> getChatroomList(@Schema(hidden = true) MemberDetailsDto memberDetailsDto);
    @Operation(summary = "채팅방 생성", description = "생성 메서드, 토큰이 필요합니다.")
    ResponseEntity<ApiResponse> createChatroom(ChatroomSaveRequest chatroomSaveRequest,
                                               @Schema(hidden = true) MemberDetailsDto memberDetailsDto);
    @Operation(summary = "채팅방 삭제", description = "토큰이 필요합니다.")
    ResponseEntity<ApiResponse> deleteChatroom(ChatroomDeleteRequest chatroomDeleteRequest,
                                               @Schema(hidden = true) MemberDetailsDto memberDetailsDto);
    @Operation(summary = "채팅방 채팅 기록 조회", description = "토큰이 필요합니다.")
    ResponseEntity<ApiResponse> getChatLog(String channelId,
                                           @Schema(hidden = true) MemberDetailsDto memberDetailsDto);
    @Operation(summary = "채팅방에 소속된 인원 중에 친구가 아닌 사용자 닉네임 조회", description = "토큰이 필요합니다.")
    ResponseEntity<ApiResponse> getNotFriendNicknameListInChatroom(String channelId,
                                                                   @Schema(hidden = true) MemberDetailsDto memberDetailsDto);
    @Operation(summary = "알람 전체 개수 조회", description = "토큰이 필요합니다.")
    ResponseEntity<ApiResponse> getAllAlertMessage(@Schema(hidden = true) MemberDetailsDto memberDetailsDto);
    @Operation(summary = "채팅방 알람 개수 조회", description = "토큰이 필요합니다.")
    ResponseEntity<ApiResponse> getChatroomAlertMessage(String channelId,
                                                        @Schema(hidden = true) MemberDetailsDto memberDetailsDto);
}

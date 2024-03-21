package nineto6.Talk.controller.friend.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import nineto6.Talk.controller.friend.request.FriendRequest;
import nineto6.Talk.dto.member.MemberDetailsDto;
import nineto6.Talk.global.common.response.ApiResponse;
import org.springframework.http.ResponseEntity;

@Tag(name = "친구")
public interface FriendControllerDocs {
    @Operation(summary = "친구 프로필 리스트 조회", description = "토큰이 필요합니다.")
    ResponseEntity<ApiResponse> getFriendProfiles(@Schema(hidden = true) MemberDetailsDto memberDetailsDto);
    @Operation(summary = "친구 등록", description = "생성 메서드, 토큰이 필요합니다.")
    ResponseEntity<ApiResponse> createFriend(FriendRequest friendRequest,
                                             @Schema(hidden = true) MemberDetailsDto memberDetailsDto);
    @Operation(summary = "친구 삭제", description = "토큰이 필요합니다.")
    ResponseEntity<ApiResponse> removeFriend(FriendRequest friendRequest,
                                             @Schema(hidden = true) MemberDetailsDto memberDetailsDto);
}

package nineto6.Talk.controller.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import nineto6.Talk.model.frined.FriendRequest;
import nineto6.Talk.model.member.MemberDetailsDto;
import nineto6.Talk.model.response.ApiResponse;
import org.springframework.http.ResponseEntity;

@Tag(name = "친구")
public interface FriendControllerDocs {
    @Operation(summary = "친구 등록", description = "친구 등록용 메서드입니다.")
    ResponseEntity<ApiResponse> createFriend(FriendRequest friendRequest,
                                             @Schema(hidden = true) MemberDetailsDto memberDetailsDto);
    @Operation(summary = "친구 삭제", description = "친구 삭제용 메서드입니다.")
    ResponseEntity<ApiResponse> removeFriend(FriendRequest friendRequest,
                                             @Schema(hidden = true) MemberDetailsDto memberDetailsDto);
}

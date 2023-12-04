package nineto6.Talk.domain.friend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Schema(title = "친구 등록")
@NoArgsConstructor
public class FriendRequest {
    @Schema(title = "친구 이름")
    private String friendNickname;

    @Builder
    public FriendRequest(String friendNickname) {
        this.friendNickname = friendNickname;
    }
}

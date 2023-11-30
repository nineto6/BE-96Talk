package nineto6.Talk.model.profile;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ProfileResponse {
    private String memberNickname;
    private String profileStateMessage;
    private String imageName;
    private String type;

    @Builder

    public ProfileResponse(String memberNickname, String profileStateMessage, String imageName, String type) {
        this.memberNickname = memberNickname;
        this.profileStateMessage = profileStateMessage;
        this.imageName = imageName;
        this.type = type;
    }
}

package nineto6.Talk.model.profile;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ProfileResponse {
    private String profileStateMessage;
    private String imageResource;

    @Builder
    public ProfileResponse(String profileStateMessage,String imageResource) {
        this.profileStateMessage = profileStateMessage;
        this.imageResource = imageResource;
    }
}

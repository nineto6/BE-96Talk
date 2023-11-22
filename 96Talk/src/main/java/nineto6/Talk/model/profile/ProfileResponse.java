package nineto6.Talk.model.profile;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ProfileResponse {
    private String profileStateMessage;
    private String resourceName;
    private String type;

    @Builder
    public ProfileResponse(String profileStateMessage, String resourceName, String type) {
        this.profileStateMessage = profileStateMessage;
        this.resourceName = resourceName;
        this.type = type;
    }
}

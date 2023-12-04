package nineto6.Talk.domain.profile.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Profile {
    private Long profileId;
    private Long memberId;
    private String profileStateMessage;
    private String profileUploadFileName;
    private String profileStoreFileName;

    @Builder
    public Profile(Long profileId, Long memberId, String profileStateMessage, String profileUploadFileName, String profileStoreFileName) {
        this.profileId = profileId;
        this.memberId = memberId;
        this.profileStateMessage = profileStateMessage;
        this.profileUploadFileName = profileUploadFileName;
        this.profileStoreFileName = profileStoreFileName;
    }
}

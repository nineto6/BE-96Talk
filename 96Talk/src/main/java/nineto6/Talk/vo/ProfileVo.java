package nineto6.Talk.vo;

import lombok.*;

import java.util.Objects;

@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProfileVo {
    private Long profileId;
    private Long memberId;
    private String profileStateMessage;
    private String profileUploadFileName;
    private String profileStoreFileName;

    @Builder
    public ProfileVo(Long profileId, Long memberId, String profileStateMessage, String profileUploadFileName, String profileStoreFileName) {
        this.profileId = profileId;
        this.memberId = memberId;
        this.profileStateMessage = profileStateMessage;
        this.profileUploadFileName = profileUploadFileName;
        this.profileStoreFileName = profileStoreFileName;
    }
}

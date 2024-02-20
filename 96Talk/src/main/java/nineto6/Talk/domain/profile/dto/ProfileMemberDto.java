package nineto6.Talk.domain.profile.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nineto6.Talk.domain.profile.domain.Profile;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProfileMemberDto {
    private String memberNickname;
    private Profile profile;

    @Builder
    public ProfileMemberDto(String memberNickname, Profile profile) {
        this.memberNickname = memberNickname;
        this.profile = profile;
    }
}

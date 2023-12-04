package nineto6.Talk.domain.profile.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProfileMember {
    private String memberNickname;
    private Profile profile;

    @Builder
    public ProfileMember(String memberNickname, Profile profile) {
        this.memberNickname = memberNickname;
        this.profile = profile;
    }
}

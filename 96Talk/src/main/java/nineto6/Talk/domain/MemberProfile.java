package nineto6.Talk.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberProfile {
    private String memberNickname;
    private Profile profile;

    @Builder
    public MemberProfile(String memberNickname, Profile profile) {
        this.memberNickname = memberNickname;
        this.profile = profile;
    }
}

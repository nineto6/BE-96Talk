package nineto6.Talk.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberProfile {
    private String memberNm;
    private Profile profile;

    @Builder
    public MemberProfile(String memberNm, Profile profile) {
        this.memberNm = memberNm;
        this.profile = profile;
    }
}

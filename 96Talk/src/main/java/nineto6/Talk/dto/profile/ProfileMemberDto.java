package nineto6.Talk.dto.profile;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nineto6.Talk.vo.ProfileVo;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProfileMemberDto {
    private String memberNickname;
    private ProfileVo profileVo;

    @Builder
    public ProfileMemberDto(String memberNickname, ProfileVo profileVo) {
        this.memberNickname = memberNickname;
        this.profileVo = profileVo;
    }
}

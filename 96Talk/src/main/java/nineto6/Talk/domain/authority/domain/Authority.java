package nineto6.Talk.domain.authority.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Authority {
    private Long authorityId;
    private Long memberId;
    private String authorityRole;
    @Builder
    public Authority(Long authorityId, Long memberId, String authorityRole) {
        this.authorityId = authorityId;
        this.memberId = memberId;
        this.authorityRole = authorityRole;
    }
}

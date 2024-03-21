package nineto6.Talk.vo;

import lombok.*;

import java.util.Objects;

@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthorityVo {
    private Long authorityId;
    private Long memberId;
    private String authorityRole;
    @Builder
    public AuthorityVo(Long authorityId, Long memberId, String authorityRole) {
        this.authorityId = authorityId;
        this.memberId = memberId;
        this.authorityRole = authorityRole;
    }
}

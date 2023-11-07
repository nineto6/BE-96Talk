package nineto6.Talk.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class JwtToken {
    private String AccessToken;
    private String RefreshToken;
}

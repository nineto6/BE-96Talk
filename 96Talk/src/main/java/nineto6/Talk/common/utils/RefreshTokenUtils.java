package nineto6.Talk.common.utils;

import nineto6.Talk.common.codes.AuthConstants;
import java.util.Optional;

public class RefreshTokenUtils {
    public static Optional<String> removePrefix(String refreshToken) {
        String prefix = AuthConstants.REFRESH_TOKEN.concat("=");
        if(refreshToken.length() < prefix.length()) {
            return Optional.empty();
        }
        return Optional.of(refreshToken.substring(prefix.length()));
    }
}

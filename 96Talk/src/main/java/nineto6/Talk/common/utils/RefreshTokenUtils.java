package nineto6.Talk.common.utils;

import java.util.Optional;

public class RefreshTokenUtils {
    public static Optional<String> removePrefix(String refreshToken) {
        String prefix = "refreshToken=";
        if(refreshToken.length() < prefix.length()) {
            return Optional.empty();
        }
        return Optional.of(refreshToken.substring(prefix.length()));
    }
}

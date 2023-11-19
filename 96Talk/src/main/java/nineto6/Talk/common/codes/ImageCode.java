package nineto6.Talk.common.codes;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public enum ImageCode {
    JPG("image/jpeg"),
    PNG("image/png"),
    GIF("image/gif"),

    ; // End

    private String code;

    ImageCode(String code) {
        this.code = code;
    }
}

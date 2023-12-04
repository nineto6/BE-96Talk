package nineto6.Talk.global.common.file.code;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public enum ImageCode {
    JPG("image/jpeg", "jpg"),
    PNG("image/png", "png"),
    GIF("image/gif", "gif"),

    ; // End

    private String mimeType;
    private String ext;

    ImageCode(String mimeType, String ext) {
        this.mimeType = mimeType;
        this.ext = ext;
    }
}

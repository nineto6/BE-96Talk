package nineto6.Talk.global.auth.redis;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value = "refresh", timeToLive = 259200) // 만료기간 3일로 지정
public class RefreshToken {
    @Id // null 로 저장될 경우 랜덤 값으로 설정됨
    private String id;
    @Indexed // 보조 인덱스 적용 (로그아웃시 필요)
    private String memberEmail;
    @Indexed // Secondary indexes(보조 인덱스) 적용
    private String refreshToken;
    private String accessToken; // 단순히 찾기 용
}

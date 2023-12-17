package nineto6.Talk.global.chat.redis;

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
@RedisHash(value = "subMember")
public class SubMember {
    @Id
    private Long id;
    private String channelId;
    @Indexed
    private String sessionId;
    private Long memberId;
    private String nickname;
}

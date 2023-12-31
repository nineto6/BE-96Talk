package nineto6.Talk.domain.chat.mongodb.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "message_log")
@Getter
@NoArgsConstructor
public class Chat {
    @Id
    private String id;
    @Field(name = "channel_id")
    private String channelId;
    @Field(name = "message")
    private String message;
    @Field(name = "writer_nick_name")
    private String writerNickname;
    @Field(name = "regdate")
    private LocalDateTime regdate;

    @Builder
    public Chat(String id, String channelId, String message, String writerNickname, LocalDateTime regdate) {
        this.id = id;
        this.channelId = channelId;
        this.message = message;
        this.writerNickname = writerNickname;
        this.regdate = regdate;
    }
}

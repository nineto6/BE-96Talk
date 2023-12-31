package nineto6.Talk.domain.chat.mongodb.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
public class RecentChat {
    private String message;
    private LocalDateTime regdate;

    @Builder
    public RecentChat(String message, LocalDateTime regdate) {
        this.message = message;
        this.regdate = regdate;
    }
}

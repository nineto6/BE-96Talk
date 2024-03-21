package nineto6.Talk.repository.chat;

import nineto6.Talk.entity.Chat;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ChatRepository extends MongoRepository<Chat, String> {
    @Query(value = "{'channel_id' : ?0}", sort = "{'regdate': 1}")
    List<Chat> findByChannelId(String channelId);
}

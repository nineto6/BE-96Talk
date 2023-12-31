package nineto6.Talk.domain.chat.mongodb.repository;

import nineto6.Talk.domain.chat.mongodb.domain.Chat;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ChatRepository extends MongoRepository<Chat, String> {
    @Query(value = "{'channel_id' : ?0}", sort = "{'regdate': 1}")
    List<Chat> findByChannelId(String channelId);
}

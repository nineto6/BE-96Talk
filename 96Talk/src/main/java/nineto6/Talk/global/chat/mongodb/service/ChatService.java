package nineto6.Talk.global.chat.mongodb.service;

import lombok.RequiredArgsConstructor;
import nineto6.Talk.global.chat.mongodb.dto.RecentChat;
import nineto6.Talk.global.chat.mongodb.domain.Chat;
import nineto6.Talk.global.chat.mongodb.dto.ChatRequest;
import nineto6.Talk.global.chat.mongodb.dto.ChatResponse;
import nineto6.Talk.global.chat.mongodb.repository.ChatRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;
    private final MongoTemplate mongoTemplate;

    @Transactional
    public ChatResponse createChat(ChatRequest chatRequest) {
        Chat chat = Chat.builder()
                .channelId(chatRequest.getChannelId())
                .writerNickname(chatRequest.getWriterNickname())
                .message(chatRequest.getMessage())
                .regdate(LocalDateTime.now())
                .build();

        chatRepository.save(chat);

        return ChatResponse.builder()
                .channelId(chat.getChannelId())
                .writerNickname(chat.getWriterNickname())
                .message(chat.getMessage())
                .regdate(chat.getRegdate())
                .build();
    }

    @Transactional(readOnly = true)
    public List<ChatResponse> findChatByChannelId(String channelId) {
        List<Chat> chatList = chatRepository.findByChannelId(channelId);

        return chatList.stream()
                .map((chat) -> ChatResponse.builder()
                        .channelId(chat.getChannelId())
                        .message(chat.getMessage())
                        .writerNickname(chat.getWriterNickname())
                        .regdate(chat.getRegdate())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public RecentChat findOneByChannelId(String channelId) {
        Query query = Query.query(Criteria
                        .where("channel_id")
                        .is(channelId))
                .with(Sort.by(Sort.Direction.DESC, "regdate")).limit(1);

        List<Chat> chatList = mongoTemplate.find(query, Chat.class);

        List<RecentChat> recentChatList = chatList.stream()
                .map((chat) -> RecentChat.builder()
                        .message(chat.getMessage())
                        .regdate(chat.getRegdate())
                        .build())
                .collect(Collectors.toList());

        if(recentChatList.size() > 0) {
            return recentChatList.get(0);
        }

        return null;
    }
}

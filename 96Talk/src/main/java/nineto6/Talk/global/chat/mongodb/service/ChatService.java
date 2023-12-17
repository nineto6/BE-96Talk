package nineto6.Talk.global.chat.mongodb.service;

import lombok.RequiredArgsConstructor;
import nineto6.Talk.domain.chatroom.dto.ChatroomMemberDto;
import nineto6.Talk.global.chat.mongodb.dto.AlertChat;
import nineto6.Talk.global.chat.mongodb.dto.RecentChat;
import nineto6.Talk.global.chat.mongodb.domain.Chat;
import nineto6.Talk.global.chat.mongodb.dto.ChatRequest;
import nineto6.Talk.global.chat.mongodb.dto.ChatResponse;
import nineto6.Talk.global.chat.mongodb.repository.ChatRepository;
import nineto6.Talk.global.error.exception.BusinessExceptionHandler;
import nineto6.Talk.global.error.exception.code.ErrorCode;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;
    private final MongoTemplate mongoTemplate;

    /**
     * 채팅 메세지 DB 에 저장
     */
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

    /**
     * 채널 아이디 값으로 채팅 메세지 전체 조회
     */
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

    /**
     * 채널 아이디 값으로 가장 최근 메세지 1개만 조회
     */
    @Transactional(readOnly = true)
    public RecentChat findOneByChannelId(String channelId) {
        Query query = Query.query(Criteria
                        .where("channel_id").is(channelId))
                .with(Sort.by(Sort.Direction.DESC, "regdate"));

        Optional<Chat> chatOptional = Optional.ofNullable(mongoTemplate.findOne(query, Chat.class));

        if(chatOptional.isEmpty()) {
            return null;
        }

        Chat chat = chatOptional.get();
        return RecentChat.builder()
                .message(chat.getMessage())
                .regdate(chat.getRegdate())
                .build();
    }

    /**
     * 채널 아이디 값과 구독 취소 시간으로 알림 메세지 반환
     */
    public List<AlertChat> findCountByChannelIdAndUnSubDate(List<ChatroomMemberDto> chatroomMemberDtoList) {
        return chatroomMemberDtoList.stream().map((chatroomMemberDto -> {
            Query query = Query.query(Criteria
                    .where("channel_id").is(chatroomMemberDto.getChannelId())
                    .and("writer_nick_name").ne(chatroomMemberDto.getMemberNickname())
                    .and("regdate").gt(chatroomMemberDto.getChatroomUnSubDate()));

            return AlertChat.builder()
                    .channelId(chatroomMemberDto.getChannelId())
                    .count(mongoTemplate.count(query, Chat.class))
                    .build();
        })).collect(Collectors.toList());
    }
}

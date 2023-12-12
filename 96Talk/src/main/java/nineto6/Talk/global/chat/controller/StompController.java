package nineto6.Talk.global.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nineto6.Talk.global.chat.mongodb.dto.ChatRequest;
import nineto6.Talk.global.chat.mongodb.dto.ChatResponse;
import nineto6.Talk.global.chat.mongodb.service.ChatService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class StompController {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatService chatService;

    /**
      * MessageMapping annotation 은 메시지의 destination 이 /hello 였다면 해당 sendMessage() method 가 불리도록 해준다.
      * - sendMessage() 에서는 simpMessagingTemplate.convertAndSend 를 통해
      *   /api/stomp/sub/chat/{channelId} 채널을 구독 중인 클라이언트에게 메시지를 전송한다.
      * - simpMessagingTemplate 는 특정 브로커로 메시지를 전달한다.
      * - 현재는 외부 브로커를 붙이지 않았으므로 인메모리에 정보를 저장한다.
      * - 메시지의 payload 는 인자(ChatRequest)로 들어온다.
     */
    @MessageMapping("/chat") // publish
    public void sendMessage(@RequestBody ChatRequest chatRequest, SimpMessageHeaderAccessor accessor) {
        log.info("Channel : {}, getWriterNm : {}, sendMessage : {}", chatRequest.getChannelId(), chatRequest.getWriterNickname(), chatRequest.getMessage());

        ChatResponse chatResponse = chatService.createChat(chatRequest);

        simpMessagingTemplate.convertAndSend("/sub/chat/" + chatResponse.getChannelId(), chatResponse);
    }
}

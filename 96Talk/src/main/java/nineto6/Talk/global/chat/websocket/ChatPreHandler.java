package nineto6.Talk.global.chat.websocket;

import io.jsonwebtoken.MalformedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nineto6.Talk.domain.chatroom.service.ChatroomService;
import nineto6.Talk.global.auth.code.AuthConstants;
import nineto6.Talk.global.auth.utils.TokenUtils;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatPreHandler implements ChannelInterceptor {
    private final ChatroomService chatroomService;
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        // headerAccessor 가 null 일 경우
        if(ObjectUtils.isEmpty(accessor)) {
            throw new MessageDeliveryException("invalid header");
        }

        if(ObjectUtils.isEmpty(accessor.getCommand())) {
            throw new MessageDeliveryException("invalid commend");
        }

        switch(accessor.getCommand()) {
            case CONNECT :
                log.info("웹소켓 연결");
                verifyAccessToken(accessor);
                break;
            case DISCONNECT :
                log.info("웹소켓 연결 해제");
                break;
            case SEND:
                log.info("메세지 전송");
                verifyAccessToken(accessor);
                // 자신에게 소속된 채팅방 인지 확인
                break;
            case SUBSCRIBE:
                log.info("메세지 구독");
                verifyChatroom(accessor);
                // 자신에게 소속된 채팅방 인지 확인
                break;
            case UNSUBSCRIBE:
                log.info("메세지 구독 해제");
                break;
            default:
                break;
        }

        return message;
    }

    /**
     * Access-Token 검증
     */
    private String verifyAccessToken(StompHeaderAccessor accessor) {
        String accessToken = getAccessToken(accessor);

        if(ObjectUtils.isEmpty(accessToken)) {
            throw new MalformedJwtException("Token is Invalid");
        }

        if(!TokenUtils.isValidAccessToken(accessToken)) {
            throw new MalformedJwtException("Token is Invalid");
        }

        return accessToken;
    }
    
    private String getAccessToken(StompHeaderAccessor accessor) {
        // 연결 요청일 경우
        String authorizationHeader = String.valueOf(accessor.getNativeHeader(AuthConstants.AUTH_HEADER));

        String authorizationHeaderStr = authorizationHeader.replace("[","").replace("]","");
        log.info("authorization Header String : {}", authorizationHeaderStr);

        // 토큰 반환
        if (authorizationHeaderStr.startsWith("Bearer ")) {
            return authorizationHeaderStr.replace("Bearer ", "");
        }

        // 없으면 null 반환
        return null;
    }

    // 자신의 채팅방인지 확인하는 메서드
    private void verifyChatroom(StompHeaderAccessor accessor) {
        if(ObjectUtils.isEmpty(accessor.getDestination())) {
            throw new MessageDeliveryException("ChatRoom does not exist");
        }
        log.info("Destination : {}", accessor.getDestination());

        String[] split = accessor.getDestination().split("/sub/chat/");
        String channelId = split[split.length - 1];
        log.info("channelId : {}", channelId);

        Long memberId = TokenUtils.getMemberIdFormAccessToken(verifyAccessToken(accessor));

        if(!chatroomService.isMyChatroom(channelId, memberId)) {
            throw new MessageDeliveryException("ChatRoom does not exist");
        }
    }
}

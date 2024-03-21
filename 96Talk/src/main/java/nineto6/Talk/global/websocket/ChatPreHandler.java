package nineto6.Talk.global.websocket;

import io.jsonwebtoken.MalformedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nineto6.Talk.service.ChatroomService;
import nineto6.Talk.global.auth.code.AuthConstants;
import nineto6.Talk.global.auth.utils.TokenUtils;
import nineto6.Talk.global.websocket.redis.SubMember;
import nineto6.Talk.global.websocket.redis.SubMemberRedisRepository;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatPreHandler implements ChannelInterceptor {
    private final ChatroomService chatroomService;
    private final SubMemberRedisRepository subMemberRedisRepository;
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
            case CONNECT:
                log.info("웹소켓 연결");
                verifyAccessToken(accessor);
            case SEND:
                log.info("메세지 전송");
                verifyAccessToken(accessor);
                break;
            case SUBSCRIBE:
                log.info("메세지 구독");
                verifyChatroomAndAlert(accessor);
                break;
            case UNSUBSCRIBE:
                log.info("메세지 구독 해제");
                removeMemberInfo(accessor);
                break;
            case DISCONNECT :
                log.info("웹소켓 연결 해제");
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

    /**
     * 헤더에서 Access-Token 을 가져온다.
     */
    private String getAccessToken(StompHeaderAccessor accessor) {
        // 연결 요청일 경우
        String authorizationHeader = String.valueOf(accessor.getNativeHeader(AuthConstants.AUTH_HEADER));

        String authorizationHeaderStr = authorizationHeader.replace("[","").replace("]","");
        log.info("authorization 토큰 : {}", authorizationHeaderStr);

        // 토큰 반환
        if (authorizationHeaderStr.startsWith("Bearer ")) {
            return authorizationHeaderStr.replace("Bearer ", "");
        }

        // 없으면 null 반환
        return null;
    }

    // 알람 또는 채팅방 구독을 확인 및 검증
    private void verifyChatroomAndAlert(StompHeaderAccessor accessor) {
        if (ObjectUtils.isEmpty(accessor.getDestination())) {
            throw new MessageDeliveryException("Chat, Alert does not Subscribe");
        }
        log.info("Destination : {}", accessor.getDestination());

        String[] split = accessor.getDestination().split("/");

        if(split[split.length - 2].equals("chat")) {
            // 채팅방 구독일 경우
            String channelId = split[split.length - 1];
            log.info("channelId : {}", channelId);

            Long memberId = TokenUtils.getMemberIdFormAccessToken(verifyAccessToken(accessor));
            String nickname = TokenUtils.getMemberNicknameFormAccessToken(getAccessToken(accessor));

            // 자신의 채팅방일 경우 통과
            if(chatroomService.isMyChatroom(channelId, memberId)) {

                // 구독 정보 저장
                chatroomService.updateChatroomSubDate(channelId, memberId);

                // Redis 에 정보 저장
                subMemberRedisRepository.save(SubMember.builder()
                        .channelId(channelId)
                        .sessionId(accessor.getSessionId())
                        .memberId(memberId)
                        .nickname(nickname)
                        .build());

                return;
            }
        }

        if(split[split.length - 2].equals("alert")) {
            // 알람 구독일 경우
            String nickname = split[split.length - 1];
            log.info("nickname : {}", nickname);

            String tokenNickname = TokenUtils.getMemberNicknameFormAccessToken(verifyAccessToken(accessor));

            // 토큰에서 가져온 닉네임과 구독 닉네임이 같을 경우
            if(nickname.equals(tokenNickname)) {
                return;
            }
        }

        throw new MessageDeliveryException("Chat, Alert does not Subscribe");
    }

    /**
     * 구독 취소할 경우
     */
    private void removeMemberInfo(StompHeaderAccessor accessor) {
        String sessionId = accessor.getSessionId();
        Optional<SubMember> connectedMemberOptional = subMemberRedisRepository.findBySessionId(sessionId);

        // 있을 경우
        if(connectedMemberOptional.isPresent()) {
            SubMember subMember = connectedMemberOptional.get();
            Long memberId = subMember.getMemberId();
            String channelId = subMember.getChannelId();

            // 구독 정보 삭제
            chatroomService.deleteChatroomSubDate(channelId, memberId);
            // 구독 취소 정보 저장
            chatroomService.updateChatroomUnSubDate(channelId, memberId);

            // 레디스에서 삭제
            subMemberRedisRepository.delete(subMember);
        }
    }
}

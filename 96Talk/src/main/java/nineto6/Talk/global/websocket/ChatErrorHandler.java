package nineto6.Talk.global.websocket;

import io.jsonwebtoken.MalformedJwtException;
import lombok.extern.slf4j.Slf4j;
import nineto6.Talk.global.error.exception.code.ErrorCode;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;

import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class ChatErrorHandler extends StompSubProtocolErrorHandler {

    public ChatErrorHandler() {
        super();
    }

    @Override
    public Message<byte[]> handleClientMessageProcessingError(Message<byte[]>clientMessage, Throwable ex) {
        Throwable exception = ex;

        if (exception instanceof MessageDeliveryException) {
            log.error("메세지 예외 : {}", exception.getMessage());
            return handleMessageDeliveryException();
        }

        if(exception instanceof MalformedJwtException) {
            log.error("멀폼 예외 : {}", exception.getMessage());
            return handleUnauthorizedException();
        }

        return super.handleClientMessageProcessingError(clientMessage, ex);
    }

    private Message<byte[]> handleUnauthorizedException() {
        return prepareErrorMessage(ErrorCode.UNAUTHORIZED_ERROR.getMessage());
    }

    private Message<byte[]> handleMessageDeliveryException() {
        return prepareErrorMessage(ErrorCode.MESSAGE_EXCEPTION_ERROR.getMessage());
    }

    private Message<byte[]> prepareErrorMessage(String message) {

        StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.ERROR);

        accessor.setMessage(message);
        accessor.setLeaveMutable(true);

        return MessageBuilder.createMessage(message.getBytes(StandardCharsets.UTF_8), accessor.getMessageHeaders());
    }
}
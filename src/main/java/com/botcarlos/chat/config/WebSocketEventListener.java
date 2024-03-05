package com.botcarlos.chat.config;

import com.botcarlos.chat.chat.ChatMessage;
import com.botcarlos.chat.chat.MessageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@Slf4j
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final SimpMessageSendingOperations messagingTemplate;

    /**
     * Listens for WebSocket session disconnect events.
     * When a user disconnects from the WebSocket session, this method retrieves their username from the session attributes,
     * logs the disconnection event, and broadcasts a LEAVE message to notify other users about the disconnection.
     *
     * @param event The WebSocket session disconnect event.
     */
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String) headerAccessor.getSessionAttributes().get("username");
        if (username != null) {
            log.info("User disconnected: {}", username);
            // Create a LEAVE message
            var chatMessage = ChatMessage.builder()
                    .type(MessageType.LEAVE)
                    .sender(username)
                    .build();
            // Broadcast the LEAVE message to all subscribers of the "/topic/public" destination
            messagingTemplate.convertAndSend("/topic/public", chatMessage);
        }
    }
}

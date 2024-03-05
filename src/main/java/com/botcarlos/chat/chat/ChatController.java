package com.botcarlos.chat.chat;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    /**
     * Method to handle messages sent by users.
     * This method listens for messages sent to "/chat.sendMessage" and forwards them to "/topic/public".
     *
     * @param chatMessage The payload of the chat message.
     * @return The received message, sent back to the public topic.
     */
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(
            @Payload ChatMessage chatMessage
    ) {
        return chatMessage;
    }

    /**
     * Method to add a user to the chat.
     * This method listens for messages sent to "/chat.addUser" and adds the username to the web socket session.
     * Then it forwards the message to "/topic/public".
     *
     * @param chatMessage The payload of the chat message.
     * @param headerAccessor The header accessor of the message.
     * @return The user addition message, sent back to the public topic.
     */
    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(
            @Payload ChatMessage chatMessage,
            SimpMessageHeaderAccessor headerAccessor
    ) {
        // Adds the username in the web socket session
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }
}

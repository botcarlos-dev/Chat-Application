package com.botcarlos.chat.chat;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessage {

    /**
     * Enum representing the type of message.
     * It can be CHAT, JOIN, or LEAVE.
     */
    private MessageType type;

    /**
     * The content of the message.
     */
    private String content;

    /**
     * The sender of the message.
     */
    private String sender;

}

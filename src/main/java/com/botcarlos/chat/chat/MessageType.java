package com.botcarlos.chat.chat;

/**
 * Enum representing the type of message in the chat system.
 * It can be CHAT, JOIN, or LEAVE.
 */
public enum MessageType {

    /**
     * Represents a regular chat message.
     */
    CHAT,

    /**
     * Represents a message when a user joins the chat.
     */
    JOIN,

    /**
     * Represents a message when a user leaves the chat.
     */
    LEAVE
}

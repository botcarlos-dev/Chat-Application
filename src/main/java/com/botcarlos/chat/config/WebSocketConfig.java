package com.botcarlos.chat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * Registers STOMP endpoints (URLs) for WebSocket communication.
     * This endpoint enables SockJS fallback options for browsers that don't support WebSocket.
     *
     * @param registry The registry for STOMP endpoints.
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").withSockJS();
    }

    /**
     * Configures message broker options.
     * This method allows us to customize the message broker configuration, including setting destination prefixes and enabling simple brokers.
     *
     * @param registry The registry for configuring message broker options.
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app"); // Set prefix for messages that are bound for methods annotated with @MessageMapping
        registry.enableSimpleBroker("/topic"); // Enables a simple in-memory message broker that is responsible for broadcasting messages to connected clients
    }
}

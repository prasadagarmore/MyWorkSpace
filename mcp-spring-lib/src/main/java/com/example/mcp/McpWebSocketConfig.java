package com.example.mcp;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * Registers the WebSocket handler when transport is configured.
 */
@Configuration
@EnableWebSocket
@ConditionalOnProperty(name = "mcp.transport", havingValue = "ws")
public class McpWebSocketConfig implements WebSocketConfigurer {
    private final McpWebSocketHandler handler;

    public McpWebSocketConfig(McpWebSocketHandler handler) {
        this.handler = handler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(handler, "/mcp");
    }
}

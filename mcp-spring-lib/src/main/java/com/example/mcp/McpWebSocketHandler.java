package com.example.mcp;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * JSON-RPC over WebSocket handler.
 */
@Component
@ConditionalOnProperty(name = "mcp.transport", havingValue = "ws")
public class McpWebSocketHandler extends TextWebSocketHandler {
    private final McpJsonRpcHandler handler;
    private final ObjectMapper mapper = new ObjectMapper();

    public McpWebSocketHandler(McpJsonRpcHandler handler) {
        this.handler = handler;
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        Map<String, Object> request = mapper.readValue(message.getPayload(), Map.class);
        Map<String, Object> response = handler.handle(request);
        session.sendMessage(new TextMessage(mapper.writeValueAsString(response)));
    }
}

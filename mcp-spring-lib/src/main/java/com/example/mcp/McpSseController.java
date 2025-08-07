package com.example.mcp;

import java.util.Map;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * JSON-RPC over Server-Sent Events.
 */
@RestController
@ConditionalOnProperty(name = "mcp.transport", havingValue = "sse")
public class McpSseController {
    private final McpJsonRpcHandler handler;

    public McpSseController(McpJsonRpcHandler handler) {
        this.handler = handler;
    }

    @PostMapping(path = "/mcp", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter handle(@RequestBody Map<String, Object> request) throws Exception {
        SseEmitter emitter = new SseEmitter();
        Map<String, Object> response = handler.handle(request);
        emitter.send(response);
        emitter.complete();
        return emitter;
    }
}

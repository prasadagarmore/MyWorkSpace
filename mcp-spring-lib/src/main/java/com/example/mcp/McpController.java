package com.example.mcp;

import java.util.Map;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Minimal HTTP-based MCP server controller.
 */
@RestController
@ConditionalOnProperty(name = "mcp.transport", havingValue = "http", matchIfMissing = true)
public class McpController {
    private final McpJsonRpcHandler handler;

    public McpController(McpJsonRpcHandler handler) {
        this.handler = handler;
    }

    @PostMapping(path = "/mcp", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> handle(@RequestBody Map<String, Object> request) throws Exception {
        return handler.handle(request);
    }
}

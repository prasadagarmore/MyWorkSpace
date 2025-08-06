package com.example.mcp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Minimal HTTP-based MCP server controller.
 */
@RestController
public class McpController {
    private final McpToolRegistry registry;

    public McpController(McpToolRegistry registry) {
        this.registry = registry;
    }

    @PostMapping(path = "/mcp", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> handle(@RequestBody Map<String, Object> request) throws Exception {
        String method = (String) request.get("method");
        Object id = request.get("id");
        Map<String, Object> result = new HashMap<>();

        if ("list_tools".equals(method)) {
            result.put("tools", registry.listTools());
        } else if ("call_tool".equals(method)) {
            Map<String, Object> params = (Map<String, Object>) request.get("params");
            String name = params != null ? (String) params.get("name") : null;
            Object output = registry.call(name);
            if (output != null) {
                Map<String, Object> content = Map.of("type", "text", "text", output.toString());
                result.put("content", List.of(content));
            } else {
                result.put("error", "Unknown tool");
            }
        } else {
            result.put("error", "Unknown method");
        }

        Map<String, Object> response = new HashMap<>();
        response.put("jsonrpc", "2.0");
        response.put("id", id);
        response.put("result", result);
        return response;
    }
}

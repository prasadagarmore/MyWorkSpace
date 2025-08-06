package com.example.mcpdemo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class McpController {
    private final HelloService service;

    public McpController(HelloService service) {
        this.service = service;
    }

    @PostMapping(path = "/mcp", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> handle(@RequestBody Map<String, Object> request) {
        String method = (String) request.get("method");
        Object id = request.get("id");
        Map<String, Object> result = new HashMap<>();

        if ("list_tools".equals(method)) {
            Map<String, Object> tool = new HashMap<>();
            tool.put("name", "hello");
            tool.put("description", "Returns a friendly greeting");
            tool.put("inputSchema", Map.of("type", "object", "properties", Map.of(), "required", List.of()));
            result.put("tools", List.of(tool));
        } else if ("call_tool".equals(method)) {
            Map<String, Object> params = (Map<String, Object>) request.get("params");
            String name = params != null ? (String) params.get("name") : null;
            if ("hello".equals(name)) {
                String greeting = service.getGreeting();
                Map<String, Object> content = Map.of("type", "text", "text", greeting);
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

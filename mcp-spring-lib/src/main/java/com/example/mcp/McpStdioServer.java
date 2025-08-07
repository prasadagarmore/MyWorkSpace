package com.example.mcp;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Minimal STDIO server for MCP.
 */
@Component
@ConditionalOnProperty(name = "mcp.transport", havingValue = "stdio")
public class McpStdioServer implements CommandLineRunner {
    private final McpJsonRpcHandler handler;
    private final ObjectMapper mapper = new ObjectMapper();

    public McpStdioServer(McpJsonRpcHandler handler) {
        this.handler = handler;
    }

    @Override
    public void run(String... args) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.isBlank()) {
                continue;
            }
            Map<String, Object> request = mapper.readValue(line, Map.class);
            Map<String, Object> response = handler.handle(request);
            System.out.println(mapper.writeValueAsString(response));
            System.out.flush();
        }
    }
}

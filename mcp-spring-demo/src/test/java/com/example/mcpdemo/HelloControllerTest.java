package com.example.mcpdemo;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HelloControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void helloReturnsGreeting() {
        String body = restTemplate.getForObject("/hello", String.class);
        assertThat(body).isEqualTo("Hello World");
    }

    @Test
    void mcpListsAndCallsTool() {
        Map<String, Object> listReq = Map.of(
            "jsonrpc", "2.0",
            "id", 1,
            "method", "list_tools"
        );
        Map listResp = restTemplate.postForObject("/mcp", listReq, Map.class);
        List<Map<String, Object>> tools = (List<Map<String, Object>>) ((Map) listResp.get("result")).get("tools");
        assertThat(tools).extracting("name").contains("hello");

        Map<String, Object> callReq = Map.of(
            "jsonrpc", "2.0",
            "id", 2,
            "method", "call_tool",
            "params", Map.of("name", "hello")
        );
        Map callResp = restTemplate.postForObject("/mcp", callReq, Map.class);
        List<Map<String, Object>> content = (List<Map<String, Object>>) ((Map) callResp.get("result")).get("content");
        assertThat(content.get(0).get("text")).isEqualTo("Hello World");
    }
}

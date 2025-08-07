package com.example.mcpdemo;

import com.example.mcp.McpTool;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    private final HelloService service;

    public HelloController(HelloService service) {
        this.service = service;
    }

    @McpTool(value = "hello", description = "Returns a friendly greeting")
    @GetMapping("/hello")
    public String hello() {
        return service.getGreeting();
    }
}

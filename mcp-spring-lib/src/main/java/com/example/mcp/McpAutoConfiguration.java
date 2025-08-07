package com.example.mcp;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Auto configuration that brings in the MCP components.
 */
@Configuration
@ComponentScan("com.example.mcp")
public class McpAutoConfiguration {
}

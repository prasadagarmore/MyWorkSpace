package com.example.mcp;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to expose a Spring MVC handler method as an MCP tool.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface McpTool {
    /**
     * Unique tool name.
     */
    String value();

    /**
     * Human friendly description.
     */
    String description() default "";
}

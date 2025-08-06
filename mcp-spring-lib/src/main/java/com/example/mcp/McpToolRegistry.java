package com.example.mcp;

import jakarta.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.RestController;

/**
 * Registry that discovers and invokes MCP tools.
 */
@Component
public class McpToolRegistry {
    private final ApplicationContext context;
    private final Map<String, ToolMethod> tools = new HashMap<>();

    @Autowired
    public McpToolRegistry(ApplicationContext context) {
        this.context = context;
    }

    @PostConstruct
    public void init() {
        Map<String, Object> beans = context.getBeansWithAnnotation(RestController.class);
        for (Object bean : beans.values()) {
            Class<?> targetClass = AopUtils.getTargetClass(bean);
            ReflectionUtils.doWithMethods(targetClass, method -> {
                McpTool annotation = AnnotationUtils.findAnnotation(method, McpTool.class);
                if (annotation != null) {
                    tools.put(annotation.value(), new ToolMethod(bean, method, annotation));
                }
            });
        }
    }

    public List<Map<String, Object>> listTools() {
        List<Map<String, Object>> list = new ArrayList<>();
        tools.forEach((name, tm) -> {
            Map<String, Object> tool = new HashMap<>();
            tool.put("name", name);
            tool.put("description", tm.annotation.description());
            tool.put("inputSchema", Map.of("type", "object", "properties", Map.of(), "required", List.of()));
            list.add(tool);
        });
        return list;
    }

    public Object call(String name) throws Exception {
        ToolMethod tm = tools.get(name);
        if (tm == null) {
            return null;
        }
        return tm.method.invoke(tm.bean);
    }

    private static class ToolMethod {
        final Object bean;
        final Method method;
        final McpTool annotation;

        ToolMethod(Object bean, Method method, McpTool annotation) {
            this.bean = bean;
            this.method = method;
            this.annotation = annotation;
        }
    }
}

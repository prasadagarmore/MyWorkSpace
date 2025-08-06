# MCP Spring Boot Demo

This repository contains a small Spring Boot project that exposes a normal REST
endpoint and makes the same functionality available as a Model Context
Protocol (MCP) tool over HTTP.

## Architecture

The demo application runs with Spring Boot's embedded Tomcat servlet
container.  Standard REST controllers are mapped as usual using `@RestController`
and `@GetMapping`.  The library module provides an `@McpTool` annotation and a
minimal JSON-RPC controller that publishes these methods under a `/mcp`
endpoint.

When the application starts:

1. Spring Boot creates the embedded Tomcat instance that serves HTTP requests.
2. `McpToolRegistry` scans all `@RestController` beans for methods annotated
   with `@McpTool` and registers them.
3. `McpController` handles POST requests to `/mcp` and dispatches MCP
   commands such as `list_tools` and `call_tool`.

Both the REST endpoint and the MCP endpoint share the same Tomcat instance and
application context, so they can call the same service methods.

## Running the demo

```bash
mvn -pl mcp-spring-demo spring-boot:run
```

Once running, the following HTTP requests are available:

* `GET /hello` – returns "Hello World"
* `POST /mcp` – JSON-RPC endpoint.  Example body:

```json
{ "jsonrpc": "2.0", "id": 1, "method": "call_tool", "params": { "name": "hello" } }
```

## Pros and Cons

**Pros**

* Single deployment: REST and MCP tools live in the same Spring Boot/Tomcat
  application.
* Familiar programming model using Spring annotations.
* Minimal boilerplate – only add `@McpTool` to expose an existing controller
  method.

**Cons**

* Only supports HTTP transport; no stdio or WebSocket integration yet.
* Tool discovery relies on scanning controllers at startup, which may add a
  small overhead for large applications.
* Error handling and authentication are minimal for demonstration purposes.

## Testing

Unit tests can be executed with:

```bash
mvn -q test
```

(The build currently requires internet access to download Spring Boot
artifacts.)


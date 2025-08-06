package com.example.mcpdemo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HelloControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void helloReturnsGreeting() {
        String body = restTemplate.getForObject("/hello", String.class);
        assertThat(body).isEqualTo("Hello World");
    }
}

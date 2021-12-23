package blog.comments.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.HashMap;

@RestController
public class DemoController {
    @Value("${spring.data.mongodb.host:unknown}")
    String host;
    @Value("${spring.data.mongodb.password:unknown}")
    String password;

    @Value("${spring.data.mongodb.user:unknown}")
    String user;

    @Value("${CONFIG_NAME}")
    String config_name;

    @Value("${CONFIG_NAMESPACE}")
    String config_namespace;

    @GetMapping
    Mono<Object> getData() {
        var properties = new HashMap<String, String>();
        properties.put("CONFIG_NAME", config_name);
        properties.put("CONFIG_NAMESPACE", config_namespace);
        properties.put("host", host);
        properties.put("password", password);
        properties.put("user", user);

        return Mono.just(properties);
    }
}

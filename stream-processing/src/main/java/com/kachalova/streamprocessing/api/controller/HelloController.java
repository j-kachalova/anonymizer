package com.kachalova.streamprocessing.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/hello")
public class HelloController {

    @GetMapping
    public Mono<String> sayHello() {
        return Mono.just("Hello from Stream Processing!");
    }
}

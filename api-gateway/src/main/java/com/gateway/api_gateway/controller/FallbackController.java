package com.gateway.api_gateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class FallbackController {

    @RequestMapping("/circuitBreaker/fallback")
    public Mono<String> circuitBreakerFoodFallback() {
        return Mono.just("Food Service is down. Contact to service provider.");
    }

}

package com.gateway.api_gateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import java.time.Duration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes().
                route(
                        "food-service",
                        route ->
                                route.path("/foods/**")
                                        .filters(f ->
                                                f.rewritePath("/foods/?(?<remaining>.*)", "/${remaining}")
                                                        .circuitBreaker(circuitBreakerConfig ->
                                                                circuitBreakerConfig.setName("circuitBreakerFood")
                                                                        .setFallbackUri("forward:/circuitBreaker/fallback")
                                                        )
                                        )
                                        .uri("lb://food-service")
                ).
                route(
                        "restaurant-service",
                        route -> route.path("/restaurants/**")
                                .filters(f ->

                                        f.rewritePath("/restaurants/?(?<remaining>.*)", "/${remaining}").
                                                addRequestHeader("ExtraValue", "Samosa")
                                )
                                .uri("lb://restaurant-service")


                ).
                build();
    }
}

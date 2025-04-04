package com.gateway.api_gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity //because the project is reactive else @EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private Converter roleConverter;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity httpSecurity) {
        httpSecurity.cors(cors-> cors.disable())
                .csrf(csrf->csrf.disable())
                .authorizeExchange(exchange->
                        exchange.pathMatchers(HttpMethod.GET).permitAll()
                                .pathMatchers("/foods/**").hasRole("ADMIN")
                                .pathMatchers(HttpMethod.POST, "/restaurants/**").hasRole("ADMIN")
                                .anyExchange().authenticated());

        //this is resource server: configure jwt when role based is not required with defaults
//        httpSecurity.oauth2ResourceServer(server->server.jwt(Customizer.withDefaults()));

        //configure jwt when role based is required for authorization of resources
        httpSecurity.oauth2ResourceServer(server->server.jwt(
                jwt -> jwt.jwtAuthenticationConverter(roleExtractor())));

        return httpSecurity.build();
    }

    public Converter<Jwt, Mono<AbstractAuthenticationToken>> roleExtractor() {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(roleConverter);
        return new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter);
    }
}

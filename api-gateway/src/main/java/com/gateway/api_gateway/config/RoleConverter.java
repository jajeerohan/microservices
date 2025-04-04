package com.gateway.api_gateway.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Configuration
public class RoleConverter implements Converter<Jwt, Collection> {

    /**
     * Convert the source object of type {@code S} to target type {@code T}.
     *
     * @param source the source object to convert, which must be an instance of {@code S} (never {@code null})
     * @return the converted object, which must be an instance of {@code T} (potentially {@code null})
     * @throws IllegalArgumentException if the source cannot be converted to the desired target type
     */
    @Override
    public Collection convert(Jwt source) {

        Map<String, Object> realmAccess = (Map<String, Object>) source.getClaims().get("realm_access");
        List<String> roles = (List<String>) realmAccess.get("roles");
        List<SimpleGrantedAuthority> rolesList = roles.stream().map(role->new SimpleGrantedAuthority("ROLE_" + role)).toList();
        return rolesList;
    }
}

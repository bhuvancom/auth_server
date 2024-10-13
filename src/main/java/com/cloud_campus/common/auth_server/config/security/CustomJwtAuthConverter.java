package com.cloud_campus.common.auth_server.config.security;

import com.cloud_campus.common.auth_server.models.JwtClaims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.Jsr310Converters;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.util.*;

/**
 * Author  : Bhuvaneshvar
 * Project : auth_server
 * Date    : 11:10 am
 **/
@Slf4j
public class CustomJwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {
    @Override
    public AbstractAuthenticationToken convert(Jwt source) {
        String userName = source.getClaimAsString(JwtClaims.email);
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        Object organization = source.getClaims().get(JwtClaims.organization_name);
        if (Objects.isNull(organization) || StringUtils.isBlank(organization.toString())) {
            throw new AccessDeniedException("User does not have any organization attached");
        }
        if (source.hasClaim(JwtClaims.roles)) {
            authorities = extractAuthorities(source.getClaim(JwtClaims.roles));
        }
        return new JwtAuthenticationToken(source, authorities, userName);
    }

    private List<SimpleGrantedAuthority> extractAuthorities(Object claim) {
        if (claim instanceof List list) {
            return ((List<String>) list).stream()
                    .filter(Objects::nonNull)
                    .map(String::toUpperCase)
                    .map(SimpleGrantedAuthority::new)
                    .toList();
        }
        return Collections.emptyList();
    }
}

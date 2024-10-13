package com.cloud_campus.common.auth_server.config;

import com.cloud_campus.common.auth_server.models.JwtClaims;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Stream;

/**
 * Author  : Bhuvaneshvar
 * Project : auth_server
 * Date    : 1:37 pm
 **/

public class WithJwtSecurityContext implements WithSecurityContextFactory<WithAuthentication> {
    @Override
    public SecurityContext createSecurityContext(WithAuthentication annotation) {
        SecurityContext emptyContext = SecurityContextHolder.createEmptyContext();
        Jwt jwt = Jwt.withTokenValue("some token")
                .subject(annotation.email())
                .issuedAt(Instant.now())
                .claim(JwtClaims.active, true)
                .claim(JwtClaims.organization_id, annotation.orgId())
                .claim(JwtClaims.roles, annotation.roles())
                .header("typ", "JWT")
                .expiresAt(Instant.now().plus(5, ChronoUnit.SECONDS))
                .build();
        List<SimpleGrantedAuthority> list = Stream.of(annotation.roles()).map(SimpleGrantedAuthority::new).toList();
        Authentication token = new JwtAuthenticationToken(jwt, list, annotation.email());
        emptyContext.setAuthentication(token);
        return emptyContext;
    }
}

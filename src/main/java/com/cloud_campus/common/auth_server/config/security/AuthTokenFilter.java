package com.cloud_campus.common.auth_server.config.security;

import com.cloud_campus.common.auth_server.config.security.jwt.JwtService;
import com.cloud_campus.common.auth_server.models.JwtClaims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.*;

/**
 * Author  : Bhuvaneshvar
 * Project : auth_server
 * Date    : 6:39 pm
 **/
@Slf4j
@RequiredArgsConstructor
@Component
public class AuthTokenFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
//    private final UserDetailsServiceImplForSecurity userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = parseJwt(request);
        if (StringUtils.isBlank(jwt)) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            JwtDecoder jwtDecoder = NimbusJwtDecoder.withPublicKey(jwtService.loadPublicKey()).build();
            final Jwt jwtToken = jwtDecoder.decode(jwt);
            String userName = jwtService.extractUserName(jwtToken);
            if (StringUtils.isNotEmpty(userName) &&
                    SecurityContextHolder.getContext().getAuthentication() == null) {

                UserDetails userDetails = convertToUserDetails(jwtToken);
                if (jwtService.isTokenValid(jwtToken, userDetails.getUsername())) {
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                    securityContext.setAuthentication(authentication);
                    SecurityContextHolder.setContext(securityContext);
                }
            }
        } catch (JwtValidationException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, exception.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    private UserDetails convertToUserDetails(Jwt jwt) {
        Boolean isActive = getBoolean(JwtClaims.active, jwt);
        return new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                Optional<Object> roles = getDataFromJwt(jwt, "roles");
                if (roles.isEmpty() || !(roles.get() instanceof List)) return Collections.emptyList();

                return ((List<String>) roles.get()).stream()
                        .map(String::toUpperCase)
                        .map(SimpleGrantedAuthority::new)
                        .toList();
            }

            @Override
            public String getPassword() {
                return null;
            }

            @Override
            public String getUsername() {
                return jwtService.extractUserName(jwt);
            }

            @Override
            public boolean isEnabled() {
                return isActive;
            }
        };
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (org.springframework.util.StringUtils.hasText(headerAuth) && headerAuth.startsWith(OAuth2AccessToken.TokenType.BEARER.getValue())) {
            return headerAuth.substring(7);
        }
        return null;
    }

    private boolean getBoolean(String key, Jwt jwt) {
        Optional<Object> dataFromJwt = getDataFromJwt(jwt, key);
        return dataFromJwt.filter(o -> BooleanUtils.toBoolean(o.toString()))
                .isPresent();
    }

    private Optional<Object> getDataFromJwt(Jwt jwt, String key) {
        Object o = null;
        if (jwt.hasClaim(key)) {
            o = jwt.getClaim(key);
        }
        return Optional.ofNullable(o);
    }

}

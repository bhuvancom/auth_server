package com.cloud_campus.common.auth_server.config.security;

import com.cloud_campus.common.auth_server.config.security.auth.AuthEntryPointJwt;
import com.cloud_campus.common.auth_server.config.security.auth.UserDetailsServiceImplForSecurity;
import com.cloud_campus.common.auth_server.config.security.jwt.JwtService;
import com.cloud_campus.common.auth_server.models.JwtClaims;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Author  : Bhuvaneshvar
 * Project : auth_server
 * Date    : 6:17 pm
 **/
@Configuration
@EnableMethodSecurity
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final UserDetailsServiceImplForSecurity userDetailsService;
    private final AuthEntryPointJwt authenticationEntryPoint;
    private final AuthTokenFilter authTokenFilter;
    private final JwtService jwtService;

    @Bean
    public BearerTokenAccessDeniedHandler accessDeniedHandler() {
        return new BearerTokenAccessDeniedHandler();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtDecoder decoder) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exception -> {
                    exception.authenticationEntryPoint(authenticationEntryPoint);
                    exception.accessDeniedHandler(accessDeniedHandler());
                })
                .oauth2ResourceServer(oAuth2 -> oAuth2
                        .jwt(jwtConfigurer -> jwtConfigurer.jwtAuthenticationConverter(customJwtConverter()).decoder(decoder))
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/api/user/auth/**").permitAll()
                                .requestMatchers("/api/test/**").permitAll()
                                .anyRequest().authenticated()
                );

//        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PermissionEvaluator permissionEvaluator() {
        return new OrganizationPermissionEvaluator();
    }

    @Bean
    public MethodSecurityExpressionHandler expressionHandler(PermissionEvaluator permissionEvaluator) {
        DefaultMethodSecurityExpressionHandler handler = new DefaultMethodSecurityExpressionHandler();
        handler.setPermissionEvaluator(permissionEvaluator);
        return handler;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(jwtService.loadPublicKey()).build();
    }

    @Bean
    public JwtAuthenticationConverter customJwtConverter() {
        JwtGrantedAuthoritiesConverter converter = new JwtGrantedAuthoritiesConverter();
        converter.setAuthoritiesClaimName(JwtClaims.roles);
        converter.setAuthorityPrefix("");
        JwtAuthenticationConverter authoritiesConverter = new JwtAuthenticationConverter();
        authoritiesConverter.setJwtGrantedAuthoritiesConverter(converter);
        return authoritiesConverter;
    }

    @Bean
    JwtEncoder jwtEncoder() {
        JWK jwk = new RSAKey.Builder(jwtService.loadPublicKey())
                .privateKey(jwtService.loadPrivateKey()).build();
        JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwkSource);
    }
}

package com.cloud_campus.common.auth_server.config.security.jwt;

import com.cloud_campus.common.auth_server.config.JwtConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {
    private final JwtConfig jwtConfig;

    @Override
    public String extractUserName(Jwt token) {
        return token.getSubject();
    }

    @Override
    public boolean isTokenValid(Jwt token, String userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails)) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(Jwt jwtToken) {
        return Objects.requireNonNull(jwtToken.getExpiresAt()).isBefore(Instant.now());
    }

    public RSAPrivateKey loadPrivateKey() {
        return jwtConfig.getSecretPrivate();
    }

    public RSAPublicKey loadPublicKey() {
        return jwtConfig.getSecret();
    }

}
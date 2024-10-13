package com.cloud_campus.common.auth_server.config.security.jwt;

import org.springframework.security.oauth2.jwt.Jwt;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public interface JwtService {
    String extractUserName(Jwt token);

    boolean isTokenValid(Jwt token, String userDetails);

    RSAPublicKey loadPublicKey();

    RSAPrivateKey loadPrivateKey();
}
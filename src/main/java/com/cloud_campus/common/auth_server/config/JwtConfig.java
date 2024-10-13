package com.cloud_campus.common.auth_server.config;

import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * Author  : Bhuvaneshvar
 * Project : auth_server
 * Date    : 6:09 pm
 **/
@Getter
@Data
@ConfigurationProperties(prefix = "cloud-campus.security")
@Component
public class JwtConfig {
    private RSAPublicKey secret;
    private RSAPrivateKey secretPrivate;
    private int expireMs;
}

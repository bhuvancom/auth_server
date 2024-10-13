package com.cloud_campus.common.auth_server.config.security.jwt;

import com.cloud_campus.common.auth_server.config.JwtConfig;
import com.cloud_campus.common.auth_server.entity.ApplicationUser;
import com.cloud_campus.common.auth_server.entity.Role;
import com.cloud_campus.common.auth_server.models.JwtClaims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtTokenGenerator {

    private final JwtEncoder jwtEncoder;
    private final JwtConfig jwtConfig;

    public String generateAccessToken(ApplicationUser userDetails) {

        log.info("[JwtTokenGenerator:generateAccessToken] Token Creation Started for:{}", userDetails.getEmail());
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put(JwtClaims.active, userDetails.isActive());
        extraClaims.put(JwtClaims.email, userDetails.getEmail());
        extraClaims.put(JwtClaims.roles, userDetails.getRoles().stream().map(Role::getName).map(String::toUpperCase).toList());
        extraClaims.put(JwtClaims.shouldChangePassword, userDetails.isChangePassword());
        extraClaims.put(JwtClaims.gender, userDetails.getGender());
        extraClaims.put(JwtClaims.firstName, userDetails.getFirstName());
        extraClaims.put(JwtClaims.lastName, userDetails.getLastName());
        extraClaims.put(JwtClaims.id, userDetails.getId());
        extraClaims.put(JwtClaims.organization_id, userDetails.getOrganization().getId());
        extraClaims.put(JwtClaims.organization_name, userDetails.getOrganization().getName());
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("cc_Auth_Server")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plus(jwtConfig.getExpireMs(), ChronoUnit.MILLIS))
                .subject(userDetails.getEmail())
                .claims(c -> c.putAll(extraClaims))
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }


    public String generateRefreshToken(ApplicationUser applicationUser) {

        log.info("[JwtTokenGenerator:generateRefreshToken] Token Creation Started for:{}", applicationUser.getEmail());

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("cc_Atuh_Server")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plus(15, ChronoUnit.DAYS))
                .subject(applicationUser.getEmail())
                .claim("scope", "REFRESH_TOKEN")
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
package com.cloud_campus.common.auth_server.config.security;

import com.cloud_campus.common.auth_server.models.JwtClaims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.io.Serializable;

/**
 * Author  : Bhuvaneshvar
 * Project : auth_server
 * Date    : 1:06 pm
 **/
@Slf4j
public class OrganizationPermissionEvaluator implements PermissionEvaluator {
    @Override
    public boolean hasPermission(Authentication authentication, Object target, Object requestOrg) {
        if (!JwtClaims.organization_id.equals(target)) {
            log.warn("Only org_id can be evaluated, found {}", target);
            return false;
        }
        JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) authentication;
        Jwt token = authenticationToken.getToken();
        String userOrg = extractUserOrg(token);
        if (StringUtils.isBlank(userOrg)) {
            log.warn("User does not have org attached {}", authentication.getName());
            return false;
        }
        return userHasAccessToOrg(userOrg, (String) requestOrg);
    }

    private String extractUserOrg(Jwt token) {
        if (token.hasClaim(JwtClaims.organization_id)) return token.getClaimAsString(JwtClaims.organization_id);
        return null;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        return false;
    }

    private boolean userHasAccessToOrg(String userOrg, String requestedOrg) {
        return userOrg != null && userOrg.equalsIgnoreCase(requestedOrg);
    }
}

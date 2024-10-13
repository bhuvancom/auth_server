package com.cloud_campus.common.auth_server.config.security.auth;

import com.cloud_campus.common.auth_server.entity.ApplicationUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * Author  : Bhuvaneshvar
 * Project : auth_server
 * Date    : 6:20 pm
 **/
@RequiredArgsConstructor
public class UserDetailsForAuth implements UserDetails {
    private final ApplicationUser user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName())).toList();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isEnabled() {
        return user.isActive();
    }
}

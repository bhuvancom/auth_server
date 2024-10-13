package com.cloud_campus.common.auth_server.config.security.auth;

import com.cloud_campus.common.auth_server.entity.ApplicationUser;
import com.cloud_campus.common.auth_server.respositories.UserDetailsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Author  : Bhuvaneshvar
 * Project : auth_server
 * Date    : 6:29 pm
 **/

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImplForSecurity implements UserDetailsService {
    private final UserDetailsRepository userDetailsRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ApplicationUser user = userDetailsRepository.findByUsernameOrEmail(username, username).orElseThrow(
                () -> new UsernameNotFoundException("User not found for %s".formatted(username))
        );
        return new UserDetailsForAuth(user);
    }
}

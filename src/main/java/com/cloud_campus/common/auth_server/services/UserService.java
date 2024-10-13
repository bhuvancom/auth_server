package com.cloud_campus.common.auth_server.services;

import com.cloud_campus.common.auth_server.config.security.jwt.JwtTokenGenerator;
import com.cloud_campus.common.auth_server.dto.request.LoginRequest;
import com.cloud_campus.common.auth_server.dto.request.RegisterRequest;
import com.cloud_campus.common.auth_server.entity.ApplicationUser;
import com.cloud_campus.common.auth_server.entity.Organization;
import com.cloud_campus.common.auth_server.entity.Role;
import com.cloud_campus.common.auth_server.entity.enums.Gender;
import com.cloud_campus.common.auth_server.respositories.OrganizationRepository;
import com.cloud_campus.common.auth_server.respositories.RoleRepository;
import com.cloud_campus.common.auth_server.respositories.UserDetailsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

/**
 * Author  : Bhuvaneshvar
 * Project : auth_server
 * Date    : 4:23 pm
 **/
@Service
@Slf4j
@RequiredArgsConstructor
public class UserService implements CommandLineRunner {
    private final UserDetailsRepository userDetailsRepository;
    private final RoleRepository roleRepository;
    private final OrganizationRepository organizationRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenGenerator jwtTokenGenerator;
    private final PasswordEncoder passwordEncoder;

    public ApplicationUser getApplicationUserByEmailOrUserName(String identity) {
        return userDetailsRepository.findByUsernameOrEmail(identity, identity)
                .orElseThrow(() -> new UsernameNotFoundException("User not found for %s".formatted(identity)));
    }

    public String login(LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        if (authenticate.isAuthenticated()) {
            ApplicationUser user = getApplicationUserByEmailOrUserName(loginRequest.getUsername());
            return jwtTokenGenerator.generateAccessToken(user);
        }

        throw new RuntimeException("Fuckk");
    }

    public String signup(RegisterRequest request) {
        return "";
    }

    @Override
    public void run(String... args) throws Exception {
        String cloudCampusFirstStart = System.getenv("cloud_campus_first_start");
        if (StringUtils.isNotEmpty(cloudCampusFirstStart)) {
            Role role = Role.builder()
                    .description("test")
                    .name("platform")
                    .build();
            Organization o = Organization.builder()
                    .name("platform")
                    .description("The platform")
                    .build();
            o = organizationRepository.save(o);
            role = roleRepository.save(role);
            UUID uuid = UUID.randomUUID();
            String encode = passwordEncoder.encode(uuid.toString());
            ApplicationUser user = ApplicationUser
                    .builder()
                    .active(true)
                    .email("bhn@qaf.com")
                    .roles(Set.of(role))
                    .gender(Gender.MALE)
                    .username("bhuancom")
                    .password(encode)
                    .firstName("Bhuvan")
                    .lastName("Srivastava")
                    .organization(o)
                    .build();
            userDetailsRepository.save(user);
            log.info("uuid {}, encoded {}", uuid, encode);
        }
    }

    public String register(RegisterRequest request) {
        return null;
    }
}

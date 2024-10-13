package com.cloud_campus.common.auth_server.controller;

import com.cloud_campus.common.auth_server.dto.request.LoginRequest;
import com.cloud_campus.common.auth_server.dto.request.RegisterRequest;
import com.cloud_campus.common.auth_server.dto.response.LoginResponse;
import com.cloud_campus.common.auth_server.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

/**
 * Author  : Bhuvaneshvar
 * Project : auth_server
 * Date    : 4:22 pm
 **/
@RestController
@RequestMapping("/api/user")
@Validated
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/auth/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest loginRequest) {
        String token = userService.login(loginRequest);
        return LoginResponse.builder().token(token).build();
    }

    @PostMapping("/auth/register")
    public LoginResponse register(@Valid @RequestBody RegisterRequest request) {
        String token = userService.register(request);
        return LoginResponse.builder().token(token).build();
    }

    @GetMapping("/test/{orgId}")
    @PreAuthorize("hasAuthority('PLATFORM') and hasPermission('organization_id', #orgId)")
    public String test(@PathVariable String orgId) {
        return "ok %s".formatted(orgId);
    }
}

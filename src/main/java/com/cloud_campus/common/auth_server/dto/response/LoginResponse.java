package com.cloud_campus.common.auth_server.dto.response;

import lombok.Builder;
import lombok.Getter;

/**
 * Author  : Bhuvaneshvar
 * Project : auth_server
 * Date    : 7:50 pm
 **/
@Builder
@Getter
public class LoginResponse {
    private String token;
}

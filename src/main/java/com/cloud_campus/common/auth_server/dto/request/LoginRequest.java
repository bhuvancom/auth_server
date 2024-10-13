package com.cloud_campus.common.auth_server.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * Author  : Bhuvaneshvar
 * Project : auth_server
 * Date    : 4:24 pm
 **/
@Data
public class LoginRequest {
    @NotEmpty private String username;
    @NotEmpty private String password;
}

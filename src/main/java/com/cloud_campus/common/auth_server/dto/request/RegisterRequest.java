package com.cloud_campus.common.auth_server.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Author  : Bhuvaneshvar
 * Project : auth_server
 * Date    : 8:13 pm
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    private String firstName;
}

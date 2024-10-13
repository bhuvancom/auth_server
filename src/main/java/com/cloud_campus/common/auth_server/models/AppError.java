package com.cloud_campus.common.auth_server.models;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 * Author  : Bhuvaneshvar
 * Project : auth_server
 * Date    : 7:38 pm
 **/
@Data
@Builder
public class AppError {
    private String message;
    private int code;
    private HttpStatus httpStatus;
}

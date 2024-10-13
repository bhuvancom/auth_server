package com.cloud_campus.common.auth_server.exception;

import com.cloud_campus.common.auth_server.models.AppError;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * Author  : Bhuvaneshvar
 * Project : auth_server
 * Date    : 7:39 pm
 **/
@RestControllerAdvice
@Slf4j
public class WebExceptionHandler {
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public AppError resourceNotFoundException(NoHandlerFoundException ex) {
        return AppError.builder()
                .code(ex.getStatusCode().value())
                .message("Not found")
                .httpStatus(HttpStatus.NOT_FOUND)
                .build();
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public AppError handleException(HttpMessageNotReadableException exception, HttpServletRequest request) {
        return AppError.builder().message("You gave an incorrect value for %s".formatted(exception.getMessage()))
                .httpStatus(HttpStatus.BAD_REQUEST)
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public AppError handleException(MethodArgumentNotValidException exception) {
        return AppError.builder().message("Argument is incorrect. Error - %s".formatted(exception.getMessage()))
                .httpStatus(HttpStatus.BAD_REQUEST)
                .build();
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public AppError handleException(AccessDeniedException exception) {
        return AppError.builder().message("Unable to access due to - %s".formatted(exception.getMessage()))
                .httpStatus(HttpStatus.FORBIDDEN)
                .build();
    }


    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public AppError handleException(BadCredentialsException exception) {
        return AppError.builder().message("Incorrect credentials")
                .httpStatus(HttpStatus.FORBIDDEN)
                .build();
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public AppError handleException(UsernameNotFoundException exception) {
        return AppError.builder().message("User not found %s".formatted(exception.getMessage()))
                .httpStatus(HttpStatus.FORBIDDEN)
                .build();
    }

    @ExceptionHandler(DisabledException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public AppError handleException(DisabledException exception) {
        return AppError.builder().message("User is disabled - %s".formatted(exception.getMessage()))
                .httpStatus(HttpStatus.FORBIDDEN)
                .build();
    }

    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public AppError handleException(NoResourceFoundException exception) {
        return AppError.builder().message("This path is not found")
                .httpStatus(HttpStatus.NOT_FOUND)
                .build();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public AppError generalError(Exception ex) {
        log.error("Error - ", ex);
        return AppError.builder()
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .code(500)
                .message("Something went wrong, please connect with support.")
                .build();
    }
}

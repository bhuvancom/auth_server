package com.cloud_campus.common.auth_server.config;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Author  : Bhuvaneshvar
 * Project : auth_server
 * Date    : 1:36 pm
 **/
@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithJwtSecurityContext.class)
public @interface WithAuthentication {
    String orgId() default "test_org";

    String[] roles() default {"PLATFORM"};

    String email() default "bhn@abc.com";
}

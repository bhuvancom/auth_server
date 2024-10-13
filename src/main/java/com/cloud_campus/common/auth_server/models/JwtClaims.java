package com.cloud_campus.common.auth_server.models;

import lombok.experimental.UtilityClass;

/**
 * Author  : Bhuvaneshvar
 * Project : auth_server
 * Date    : 7:50 pm
 **/
@UtilityClass
public class JwtClaims {
    public static final String shouldChangePassword = "change_password";
    public static final String email = "email";
    public static final String roles = "roles";
    public static final String active = "active";
    public static final String organization_id = "organization_id";
    public static final String organization_name = "organization_name";
    public static final String gender = "gender";
    public static final String firstName = "first_name";
    public static final String lastName = "last_name";
    public static final String id = "id";

}

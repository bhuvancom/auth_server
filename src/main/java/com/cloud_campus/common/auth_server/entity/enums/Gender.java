package com.cloud_campus.common.auth_server.entity.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Gender {
    MALE("male"),
    FEMALE("female");

    @JsonValue
    private final String value;
}
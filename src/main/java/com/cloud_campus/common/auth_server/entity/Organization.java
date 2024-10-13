package com.cloud_campus.common.auth_server.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;
import java.util.UUID;

/**
 * Author  : Bhuvaneshvar
 * Project : auth_server
 * Date    : 6:45 pm
 **/
@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String name;

    private String description;

    @LastModifiedDate
    @JsonIgnore
    @Builder.Default
    private Instant updatedOn = Instant.now();
}

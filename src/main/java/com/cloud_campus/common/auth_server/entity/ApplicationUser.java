package com.cloud_campus.common.auth_server.entity;

import com.cloud_campus.common.auth_server.entity.enums.Gender;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity()
@Table(name = "user_details",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        })
public class ApplicationUser {
    @Id()
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id", nullable = false, length = 255)
    private UUID id;

    private String firstName;
    private String lastName;

    @Email
    private String email;

    @NotBlank
    private String username;

    private String inviteCode;
    private boolean active;

    @NotBlank
    private String password;
    private boolean changePassword;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "org_id", referencedColumnName = "id", nullable = false)
    private Organization organization;
}


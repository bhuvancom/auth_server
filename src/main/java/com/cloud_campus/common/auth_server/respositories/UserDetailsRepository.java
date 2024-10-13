package com.cloud_campus.common.auth_server.respositories;

import com.cloud_campus.common.auth_server.entity.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserDetailsRepository extends JpaRepository<ApplicationUser, UUID> {

    Optional<ApplicationUser> findByUsernameOrEmail(String username, String email);
}
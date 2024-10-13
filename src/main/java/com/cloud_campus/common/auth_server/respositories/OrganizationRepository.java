package com.cloud_campus.common.auth_server.respositories;

import com.cloud_campus.common.auth_server.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Author  : Bhuvaneshvar
 * Project : auth_server
 * Date    : 8:20 pm
 **/
@Repository
public interface OrganizationRepository extends JpaRepository<Organization, UUID> {
}

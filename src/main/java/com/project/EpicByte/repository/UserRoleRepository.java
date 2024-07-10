package com.project.EpicByte.repository;

import com.project.EpicByte.model.entity.UserRole;
import com.project.EpicByte.model.entity.enums.UserRolesEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UUID> {
    UserRole findUserRoleByRole(UserRolesEnum userRolesEnum);
}

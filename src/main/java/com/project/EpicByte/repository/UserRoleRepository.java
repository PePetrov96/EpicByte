package com.project.EpicByte.repository;

import com.project.EpicByte.model.entity.userEntities.UserRoleEntity;
import com.project.EpicByte.model.entity.enums.UserRolesEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRoleEntity, Long> {
    UserRoleEntity findUserRoleByRole(UserRolesEnum userRolesEnum);
}

package com.project.EpicByte.repository;

import com.project.EpicByte.model.entity.userEntities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findUserByUsername(String username);
    UserEntity findUserEntityByUsername(String username);
    UserEntity findUserEntityById(Long id);
    boolean existsByUsername(String username);
}

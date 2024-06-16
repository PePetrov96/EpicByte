package com.project.EpicByte.repository;

import com.project.EpicByte.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findUserByUsername(String username);
    UserEntity findUserEntityByUsername(String username);
    UserEntity findUserEntityById(UUID id);
    boolean existsByUsername(String username);

    @Query("SELECT u FROM UserEntity u JOIN FETCH u.cartItems JOIN FETCH u.orders WHERE u.username = :username")
    UserEntity getUserEntityByUsername(@Param("username") String username);
}

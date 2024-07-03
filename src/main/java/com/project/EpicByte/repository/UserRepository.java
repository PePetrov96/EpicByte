package com.project.EpicByte.repository;

import com.project.EpicByte.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findUserByUsername(String username);
    UserEntity findUserEntityByUsername(String username);
    UserEntity findUserEntityById(UUID id);
    boolean existsByUsername(String username);

    @Query("SELECT u FROM UserEntity u JOIN FETCH u.userOrders")
    List<UserEntity> findAllUsersWithInitializedOrders();

    @Query("SELECT u FROM UserEntity u JOIN FETCH u.cartItems")
    List<UserEntity> findAllUsersWithInitializedCartItems();

    @Query("SELECT u FROM UserEntity u LEFT JOIN FETCH u.cartItems WHERE u.username = :username")
    UserEntity findUserByUsernameWithInitializedCartItems(@Param("username") String username);

    @Query("SELECT u FROM UserEntity u LEFT JOIN FETCH u.userOrders WHERE u.username = :username")
    UserEntity findUserByUsernameWithInitializedOrders(@Param("username") String username);

    @Query("SELECT u FROM UserEntity u JOIN FETCH u.cartItems WHERE u.id = :userId")
    UserEntity findUserByIdWithInitializedCartItems(@Param("userId") UUID userId);

    @Query("SELECT u FROM UserEntity u JOIN FETCH u.userOrders WHERE u.id = :userId")
    UserEntity findUserByIdWithInitializedOrders(@Param("userId") UUID userId);
}

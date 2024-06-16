package com.project.EpicByte.repository;

import com.project.EpicByte.model.entity.UserOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface UserOrderRepository extends JpaRepository<UserOrder, UUID> {
    Optional<UserOrder> findUserOrderById(UUID id);
    Set<UserOrder> findUserOrderByUserId(UUID id);
}
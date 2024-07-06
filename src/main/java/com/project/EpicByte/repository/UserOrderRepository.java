package com.project.EpicByte.repository;

import com.project.EpicByte.model.entity.UserOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface UserOrderRepository extends JpaRepository<UserOrder, UUID> {
    Optional<UserOrder> findUserOrderById(UUID id);
    Set<UserOrder> findUserOrderByUserIdOrderByOrderDateDesc(UUID id);
    Set<UserOrder> findUserOrderByUserId(UUID id);
    @Query("SELECT uo FROM UserOrder uo WHERE uo.isComplete = false ORDER BY uo.orderDate DESC")
    Set<UserOrder> findUserOrdersIncompleteOrderByOrderDateDesc();
    @Query("SELECT uo FROM UserOrder uo WHERE uo.isComplete = false")
    Set<UserOrder> findUserOrdersIncomplete();
}

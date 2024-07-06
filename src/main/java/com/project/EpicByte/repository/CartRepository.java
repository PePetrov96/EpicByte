package com.project.EpicByte.repository;

import com.project.EpicByte.model.entity.productEntities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CartRepository extends JpaRepository<CartItem, UUID> {
    CartItem findByProductId(UUID productId);
    List<CartItem> findAllByUserId(UUID id);

    @Query("SELECT ci FROM CartItem ci WHERE ci.user.username = :username")
    List<CartItem> findAllByUserUsername(@Param("username") String username);

    @Query("SELECT ci FROM CartItem ci WHERE ci.user.id = :userId AND ci.product.id = :productId")
    List<CartItem> findAllByUserIdAndProductId(@Param("userId") UUID userId, @Param("productId") UUID productId);

    @Query("SELECT ci FROM CartItem ci WHERE ci.product.id = :productId")
    List<CartItem> findAllByProductId(@Param("productId") UUID productId);
}

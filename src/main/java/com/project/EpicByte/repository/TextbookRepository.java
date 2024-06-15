package com.project.EpicByte.repository;

import com.project.EpicByte.model.entity.productEntities.Textbook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TextbookRepository extends JpaRepository<Textbook, UUID> {
    @Query("SELECT t from Textbook t WHERE t.id = :userId")
    Textbook findTextbookById(@Param("userId") UUID userId);
}

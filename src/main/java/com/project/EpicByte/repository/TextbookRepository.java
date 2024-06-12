package com.project.EpicByte.repository;

import com.project.EpicByte.model.entity.productEntities.Book;
import com.project.EpicByte.model.entity.productEntities.Textbook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TextbookRepository extends JpaRepository<Textbook, Long> {
    @Query("SELECT t from Textbook t WHERE t.id = :userId")
    Textbook findTextbookById(@Param("userId") Long userId);
}

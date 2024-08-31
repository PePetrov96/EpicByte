package com.project.EpicByte.repository.productRepositories;

import com.project.EpicByte.model.entity.BaseProduct;
import com.project.EpicByte.model.entity.productEntities.Book;
import com.project.EpicByte.model.entity.productEntities.Textbook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface TextbookRepository extends JpaRepository<Textbook, UUID> {
    @Query("SELECT t from Textbook t WHERE t.id = :userId")
    Textbook findTextbookById(@Param("userId") UUID userId);

    @Query("SELECT t FROM Textbook t WHERE t.isNewProduct = true and t.dateCreated <= :date")
    List<Textbook> findNewTextbooksOlderThanAWeek(@Param("date") LocalDate date);

    @Query("SELECT t FROM Textbook t WHERE t.productName LIKE %:productName%")
    List<BaseProduct> findTextbooksByProductName(@Param("productName") String productName);

    @Query("SELECT t FROM Textbook t WHERE t.authorName LIKE %:authorName%")
    List<BaseProduct> findTextbooksByAuthorName(@Param("authorName") String authorName);
}

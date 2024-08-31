package com.project.EpicByte.repository.productRepositories;

import com.project.EpicByte.model.entity.BaseProduct;
import com.project.EpicByte.model.entity.productEntities.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface BookRepository extends JpaRepository<Book, UUID> {
    @Query("SELECT b from Book b WHERE b.id = :userId")
    Book findBookById(@Param("userId") UUID userId);

    @Query("SELECT b FROM Book b WHERE b.isNewProduct = true and b.dateCreated <= :date")
    List<Book> findNewBooksOlderThanAWeek(@Param("date") LocalDate date);

    @Query("SELECT b FROM Book b WHERE b.productName LIKE %:productName%")
    List<BaseProduct> findBooksByProductName(@Param("productName") String productName);

    @Query("SELECT b FROM Book b WHERE b.authorName LIKE %:authorName%")
    List<BaseProduct> findBooksByAuthorName(@Param("authorName") String authorName);
}

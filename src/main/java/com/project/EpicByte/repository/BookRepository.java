package com.project.EpicByte.repository;

import com.project.EpicByte.model.entity.productEntities.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    @Query("SELECT b from Book b WHERE b.id = :userId")
    Book findBookById(@Param("userId") Long userId);
}

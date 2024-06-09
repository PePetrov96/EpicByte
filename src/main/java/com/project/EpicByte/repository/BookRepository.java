package com.project.EpicByte.repository;

import com.project.EpicByte.model.entity.productEntities.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
}

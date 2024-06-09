package com.project.EpicByte.repository;

import com.project.EpicByte.model.entity.productEntities.Textbook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TextbookRepository extends JpaRepository<Textbook, Long> {
}

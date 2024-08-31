package com.project.EpicByte.repository.productRepositories;

import com.project.EpicByte.model.entity.BaseProduct;
import com.project.EpicByte.model.entity.productEntities.Book;
import com.project.EpicByte.model.entity.productEntities.Toy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface ToyRepository extends JpaRepository<Toy, UUID> {
    @Query("SELECT t from Toy t WHERE t.id = :userId")
    Toy findToyById(@Param("userId") UUID userId);

    @Query("SELECT t FROM Toy t WHERE t.isNewProduct = true and t.dateCreated <= :date")
    List<Toy> findNewToysOlderThanAWeek(@Param("date") LocalDate date);

    @Query("SELECT t FROM Toy t WHERE t.productName LIKE %:productName%")
    List<BaseProduct> findToysByProductName(@Param("productName") String productName);
}

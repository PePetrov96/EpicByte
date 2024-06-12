package com.project.EpicByte.repository;

import com.project.EpicByte.model.entity.productEntities.Toy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ToyRepository extends JpaRepository<Toy, Integer> {
    @Query("SELECT t from Toy t WHERE t.id = :userId")
    Toy findToyById(@Param("userId") Long userId);
}

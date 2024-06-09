package com.project.EpicByte.repository;

import com.project.EpicByte.model.entity.productEntities.Toy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ToyRepository extends JpaRepository<Toy, Integer> {
}

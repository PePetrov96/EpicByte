package com.project.EpicByte.repository;

import com.project.EpicByte.model.entity.productEntities.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductImagesRepository extends JpaRepository<ProductImage, UUID> {
}
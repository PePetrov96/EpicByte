package com.project.EpicByte.model.entity.productEntities;

import com.project.EpicByte.model.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//CLOUDINARY
@Entity
@Table(name = "product_images")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ProductImage extends BaseEntity {
    @Column(name = "location_path")
    private String locationPath;
}

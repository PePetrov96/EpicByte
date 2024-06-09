package com.project.EpicByte.model.entity;

import com.project.EpicByte.model.entity.enums.ProductTypeEnum;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@MappedSuperclass
@Getter @Setter @NoArgsConstructor
public abstract class BaseProduct extends BaseEntity {
    @Enumerated(EnumType.STRING)
    @Column(name = "productType")
    private ProductTypeEnum productType;

    @Column(name = "product_image_url")
    private String productImageUrl;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_price", columnDefinition = "DECIMAL(19,2)")
    private Double productPrice;

    @Column(name = "description", columnDefinition = "LONGTEXT")
    private String description;
}

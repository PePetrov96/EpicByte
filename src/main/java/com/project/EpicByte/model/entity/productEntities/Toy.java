package com.project.EpicByte.model.entity.productEntities;

import com.project.EpicByte.model.entity.BaseProduct;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("Toy")
@Table(name = "products_toys")
@Getter @Setter @NoArgsConstructor
public class Toy extends BaseProduct {
    @Column(name = "brand")
    private String brand;
}

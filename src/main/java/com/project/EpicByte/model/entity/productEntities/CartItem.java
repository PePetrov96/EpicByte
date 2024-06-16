package com.project.EpicByte.model.entity.productEntities;

import com.project.EpicByte.model.entity.BaseEntity;
import com.project.EpicByte.model.entity.BaseProduct;
import com.project.EpicByte.model.entity.UserEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class CartItem extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private BaseProduct product;
}
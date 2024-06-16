package com.project.EpicByte.model.entity.productEntities;

import com.project.EpicByte.model.entity.BaseEntity;
import com.project.EpicByte.model.entity.BaseProduct;
import com.project.EpicByte.model.entity.UserOrder;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class OrderItem extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "product_id")
    private BaseProduct product;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "total_product_price")
    private BigDecimal totalProductPrice;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable=false)
    private UserOrder userOrder;
}

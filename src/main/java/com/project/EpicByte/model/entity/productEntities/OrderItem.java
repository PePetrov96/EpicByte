package com.project.EpicByte.model.entity.productEntities;

import com.project.EpicByte.model.entity.BaseEntity;
import com.project.EpicByte.model.entity.UserOrder;
import com.project.EpicByte.model.entity.enums.ProductTypeEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class OrderItem extends BaseEntity {
    @Enumerated(EnumType.STRING)
    @Column(name = "product_type")
    private ProductTypeEnum productType;

    @Column(name = "product_image_url")
    private String productImageUrl;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_price")
    private BigDecimal productPrice;

//    @ManyToOne
//    @JoinColumn(name = "product_id")
//    private BaseProduct product;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "total_product_price")
    private BigDecimal totalProductPrice;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable=false)
    private UserOrder userOrder;
}

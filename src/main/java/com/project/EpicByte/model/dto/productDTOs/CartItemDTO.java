package com.project.EpicByte.model.dto.productDTOs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor
public class CartItemDTO {
    private UUID id;
    private String productType;
    private String productImageUrl;
    private String productName;
    private BigDecimal productPrice;
    private int quantity;
    private BigDecimal totalPriceOfProduct;
}

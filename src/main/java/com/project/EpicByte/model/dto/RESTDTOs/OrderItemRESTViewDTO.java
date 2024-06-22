package com.project.EpicByte.model.dto.RESTDTOs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor
public class OrderItemRESTViewDTO {
    private UUID id;
    private BaseProductRESTViewDTO baseProduct;
    private int quantity;
    private BigDecimal totalProductPrice;
}

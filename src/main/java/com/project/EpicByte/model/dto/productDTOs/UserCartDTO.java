package com.project.EpicByte.model.dto.productDTOs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter @NoArgsConstructor
public class UserCartDTO {
    private List<CartItemDTO> cartItems = new ArrayList<>();
    private BigDecimal totalPrice;
}

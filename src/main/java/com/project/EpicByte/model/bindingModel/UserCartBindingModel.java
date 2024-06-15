package com.project.EpicByte.model.bindingModel;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter @NoArgsConstructor
public class UserCartBindingModel {
    private List<CartItemBindingModel> cartItems = new ArrayList<>();
    private BigDecimal totalPrice;
}

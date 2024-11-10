package com.project.EpicByte.model.dto.RESTDTOs;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter @Setter
public class CartItemRESTViewDTO {
    private UUID id;
    private BaseProductRESTViewDTO baseProduct;
}

package com.project.EpicByte.model.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public abstract class BaseAddDTO {
    @NotEmpty(message = "{product.imageUrl.empty.error.message}")
    private String productImageUrl;

    @NotEmpty(message = "{product.name.empty.error.message}")
    private String productName;

    @NotNull(message = "{product.price.empty.error.message}")
    @Positive(message = "{product.price.negative.number.error.message}")
    private Double productPrice;

    @NotEmpty(message = "{product.description.empty.error.message}")
    private String description;
}

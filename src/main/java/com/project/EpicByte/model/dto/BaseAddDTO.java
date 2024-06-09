package com.project.EpicByte.model.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public abstract class BaseAddDTO {
    @NotEmpty(message = "Please enter product image URL!")
    private String productImageUrl;

    @NotEmpty(message = "Please enter product name!")
    private String productName;

    @NotNull(message = "Please enter a price!")
    @Positive(message = "Please enter a positive price!")
    private Double productPrice;

    @NotEmpty(message = "Please enter a description!")
    private String description;
}

package com.project.EpicByte.model.dto.productDTOs;

import com.project.EpicByte.model.dto.BaseAddDTO;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class ToyAddDTO extends BaseAddDTO {
    @NotEmpty(message = "{product.brand.empty.error.message}")
    private String brand;
}

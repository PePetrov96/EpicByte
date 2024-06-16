package com.project.EpicByte.model.dto.productDTOs;

import com.project.EpicByte.model.dto.BaseAddDTO;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class MovieAddDTO extends BaseAddDTO {
    @NotEmpty(message = "{product.genre.empty.error.message}")
    private String genre;

    @NotEmpty(message = "{product.carrier.empty.error.message}")
    private String carrier;
}

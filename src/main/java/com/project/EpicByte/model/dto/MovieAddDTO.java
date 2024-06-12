package com.project.EpicByte.model.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class MovieAddDTO extends BaseAddDTO {
    @NotEmpty(message = "{product.genre.empty.error.message}")
    private String genre;

    @NotEmpty(message = "{product.language.empty.error.message}")
    private String language;

    @NotEmpty(message = "{product.carrier.empty.error.message}")
    private String carrier;
}

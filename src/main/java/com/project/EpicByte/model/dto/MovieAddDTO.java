package com.project.EpicByte.model.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class MovieAddDTO extends BaseAddDTO {
    @NotEmpty(message = "Please enter a genre!")
    private String genre;

    @NotEmpty(message = "Please select a language!")
    private String language;
}

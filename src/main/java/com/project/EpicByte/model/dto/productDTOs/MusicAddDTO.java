package com.project.EpicByte.model.dto.productDTOs;

import com.project.EpicByte.model.dto.BaseAddDTO;
import com.project.EpicByte.validation.PastDateOnly;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor
public class MusicAddDTO extends BaseAddDTO {
    @NotEmpty(message = "{product.artistName.empty.error.message}")
    private String artistName;

    @NotEmpty(message = "{product.publisher.empty.error.message}")
    private String publisher;

    @PastDateOnly
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate publicationDate;

    @NotEmpty(message = "{product.carrier.empty.error.message}")
    private String carrier;

    @NotEmpty(message = "{product.genre.empty.error.message}")
    private String genre;
}

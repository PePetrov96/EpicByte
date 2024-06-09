package com.project.EpicByte.model.dto;

import com.project.EpicByte.validation.PastDateOnly;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor
public class MusicAddDTO extends BaseAddDTO {
    @NotEmpty(message = "Please enter an artist name!")
    private String artistName;

    @NotEmpty(message = "Please enter a publisher name!")
    private String publisher;

    @PastDateOnly
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate publicationDate;

    @NotEmpty(message = "Please select a carrier!")
    private String carrier;

    @NotEmpty(message = "Please enter a genre!")
    private String genre;
}

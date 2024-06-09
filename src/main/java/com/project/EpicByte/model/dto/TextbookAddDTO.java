package com.project.EpicByte.model.dto;

import com.project.EpicByte.validation.PastDateOnly;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor
public class TextbookAddDTO extends BaseAddDTO {
    @NotEmpty(message = "Please enter an author name!")
    private String authorName;

    @NotEmpty(message = "Please enter a publisher name!")
    private String publisher;

    @PastDateOnly
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate publicationDate;

    @NotEmpty(message = "Please select a language!")
    private String language;

    @NotNull(message = "Please enter number of book pages!")
    private Integer printLength;

    @NotEmpty(message = "Please set dimensions in format 'length x width x height'!")
    private String dimensions;
}

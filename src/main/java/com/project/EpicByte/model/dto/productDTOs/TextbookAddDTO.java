package com.project.EpicByte.model.dto.productDTOs;

import com.project.EpicByte.model.dto.BaseAddDTO;
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
    @NotEmpty(message = "{product.authorName.empty.error.message}")
    private String authorName;

    @NotEmpty(message = "{product.publisher.empty.error.message}")
    private String publisher;

    @PastDateOnly
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate publicationDate;

    @NotEmpty(message = "{product.language.empty.error.message}")
    private String language;

    @NotNull(message = "{product.printLength.empty.error.message}")
    private Integer printLength;

    @NotEmpty(message = "{product.dimensions.empty.error.message}")
    private String dimensions;
}

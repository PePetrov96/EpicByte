package com.project.EpicByte.model.entity.productEntities;

import com.project.EpicByte.model.entity.BaseProduct;
import com.project.EpicByte.model.entity.enums.LanguageEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@DiscriminatorValue("Textbook")
@Table(name = "products_textbooks")
@Getter @Setter @NoArgsConstructor
public class Textbook extends BaseProduct {
    @Column(name = "author_name")
    private String authorName;

    @Column(name = "publisher")
    private String publisher;

    @Column(name = "publication_date")
    private LocalDate publicationDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "language")
    private LanguageEnum language;

    @Column(name = "print_length")
    private Integer printLength;

    @Column(name = "dimensions")
    private String dimensions;
}

package com.project.EpicByte.model.entity.productEntities;

import com.project.EpicByte.model.entity.BaseProduct;
import com.project.EpicByte.model.entity.enums.LanguageEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "products_movies")
@Getter @Setter @NoArgsConstructor
public class Movie extends BaseProduct {
    @Column(name = "genre")
    private String genre;

    @Enumerated(EnumType.STRING)
    @Column(name = "language")
    private LanguageEnum language;
}

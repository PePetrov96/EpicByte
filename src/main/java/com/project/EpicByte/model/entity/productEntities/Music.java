package com.project.EpicByte.model.entity.productEntities;

import com.project.EpicByte.model.entity.BaseProduct;
import com.project.EpicByte.model.entity.enums.MusicCarrierEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@DiscriminatorValue("Music")
@Table(name = "products_music")
@Getter @Setter @NoArgsConstructor
public class Music extends BaseProduct {
    @Column(name = "artist_name")
    private String artistName;

    @Column(name = "publisher")
    private String publisher;

    @Column(name = "publication_date")
    private LocalDate publicationDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "carrier")
    private MusicCarrierEnum carrier;

    @Column(name = "genre")
    private String genre;
}

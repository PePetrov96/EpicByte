package com.project.EpicByte.model.dto.RESTDTOs;

import com.project.EpicByte.model.entity.enums.ProductTypeEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor
public class BaseProductRESTViewDTO {
    private LocalDate dateCreated;
    private boolean isNewProduct;
    private ProductTypeEnum productType;
    private String productImageUrl;
    private String productName;
    private BigDecimal productPrice;
    private String description;
}

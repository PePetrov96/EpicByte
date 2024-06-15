package com.project.EpicByte.model.dto.productDTOs;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class OrderAddressDTO {
    @NotEmpty(message = "{address.error.text}")
    private String address;

    @NotEmpty(message = "{city.error.text}")
    private String city;

    @NotEmpty(message = "{neighborhood.error.text}")
    private String neighborhood;
}

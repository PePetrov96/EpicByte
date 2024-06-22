package com.project.EpicByte.model.dto.RESTDTOs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class UserOrderRESTViewDTO {
    private UUID id;
    private LocalDate orderDate;
    private String city;
    private String neighborhood;
    private String address;
    private BigDecimal totalCost;
    private boolean isComplete;
    private List<OrderItemRESTViewDTO> orderItems = new ArrayList<>();
}

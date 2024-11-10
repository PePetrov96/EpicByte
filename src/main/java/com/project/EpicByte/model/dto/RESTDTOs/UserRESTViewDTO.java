package com.project.EpicByte.model.dto.RESTDTOs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

@Getter @Setter @NoArgsConstructor
public class UserRESTViewDTO {
    private UUID id;
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private boolean termsAndConditionsAgreed;
    private HashSet<UserRoleRESTViewDTO> roles = new HashSet<>();
    private String imageUrl;
    private List<CartItemRESTViewDTO> cartItems = new ArrayList<>();
    private Set<UserOrderRESTViewDTO> userOrders = new HashSet<>();
}

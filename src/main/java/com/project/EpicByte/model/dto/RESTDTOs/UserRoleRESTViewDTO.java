package com.project.EpicByte.model.dto.RESTDTOs;

import com.project.EpicByte.model.entity.enums.UserRolesEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter @Setter @NoArgsConstructor
public class UserRoleRESTViewDTO {
    private UUID id;
    private UserRolesEnum role;
}

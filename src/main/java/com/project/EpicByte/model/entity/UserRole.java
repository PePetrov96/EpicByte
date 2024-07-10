package com.project.EpicByte.model.entity;

import com.project.EpicByte.model.entity.enums.UserRolesEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
public class UserRole extends BaseEntity {
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRolesEnum role;
}
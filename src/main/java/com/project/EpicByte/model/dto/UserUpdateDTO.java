package com.project.EpicByte.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class UserUpdateDTO {
    private Long id;

    @NotEmpty(message = "Please enter a name")
    private String firstName;

    @NotEmpty(message = "Please enter a name")
    private String lastName;

    @Size(min = 3, max = 20, message = "Username length must be between 3 and 20 characters.")
    private String username;

    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Invalid email address")
    private String email;
}

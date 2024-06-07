package com.project.EpicByte.model.dto;

import com.project.EpicByte.validation.PasswordMatches;
import com.project.EpicByte.validation.UniqueUsername;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
@PasswordMatches
public class UserRegisterDTO {
    @NotEmpty(message = "Please enter a name")
    private String firstName;

    @NotEmpty(message = "Please enter a name")
    private String lastName;

    @Size(min = 3, max = 20, message = "Username length must be between 3 and 20 characters.")
    @UniqueUsername
    private String username;

    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Invalid email address")
    private String email;

    @NotNull
    @Size(min = 3, message = "Password length must be minimum 3 characters.")
    private String password;

    private String repeatPassword;

    @AssertTrue(message = "You must agree to the terms and conditions!")
    private boolean termsAndConditionsAgreed;
}

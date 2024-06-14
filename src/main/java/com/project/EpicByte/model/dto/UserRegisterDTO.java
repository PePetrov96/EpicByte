package com.project.EpicByte.model.dto;

import com.project.EpicByte.validation.NotAdminUsername;
import com.project.EpicByte.validation.PasswordMatches;
import com.project.EpicByte.validation.UniqueUsername;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
@PasswordMatches
public class UserRegisterDTO {
    @NotEmpty(message = "{name.empty.error.text}")
    private String firstName;

    @NotEmpty(message = "{name.empty.error.text}")
    private String lastName;

    @Size(min = 3, max = 20, message = "{username.invalid.length.error.text}")
    @UniqueUsername
    @NotAdminUsername
    private String username;

    @NotEmpty(message = "{empty.email.error.text}")
    @Email(message = "{invalid.email.error.text}")
    private String email;

    @NotNull
    @Size(min = 3, message = "{password.invalid.length.error.text}")
    private String password;

    private String repeatPassword;

    @AssertTrue(message = "{terms.and.conditions.agreement.error.text}")
    private boolean termsAndConditionsAgreed;
}

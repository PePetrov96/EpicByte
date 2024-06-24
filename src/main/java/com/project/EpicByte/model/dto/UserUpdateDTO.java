package com.project.EpicByte.model.dto;

import com.project.EpicByte.validation.NotAdminUsername;
import com.project.EpicByte.validation.UniqueUpdateUsername;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter @Setter @NoArgsConstructor
@UniqueUpdateUsername
public class UserUpdateDTO {
    private UUID id;

    @NotEmpty(message = "{name.empty.error.text}")
    private String firstName;

    @NotEmpty(message = "{name.empty.error.text}")
    private String lastName;

    @Size(min = 3, max = 20, message = "{username.invalid.length.error.text}")
    @NotAdminUsername
    private String username;

    @NotEmpty(message = "{empty.email.error.text}")
    @Email(message = "{invalid.email.error.text}")
    private String email;
}

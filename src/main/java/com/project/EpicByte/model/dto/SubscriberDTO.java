package com.project.EpicByte.model.dto;

import com.project.EpicByte.validation.NotUniqueEmail;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class SubscriberDTO {
    @NotEmpty(message = "{name.empty.error.text}")
    private String name;

    @NotEmpty(message = "{empty.email.error.text}")
    @NotUniqueEmail
    private String email;
}

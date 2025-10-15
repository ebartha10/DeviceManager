package com.platform.device.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private UUID id;

    @NotBlank(message = "Full name is mandatory")
    private String fullName;

    @Email(message = "Email should be valid")
    private String email;

}

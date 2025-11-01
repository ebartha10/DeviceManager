package com.platform.user.dtos;

import com.platform.user.entities.Role;
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

    private Role role;

    @Email(message = "Email should be valid")
    private String email;

}

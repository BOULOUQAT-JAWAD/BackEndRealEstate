package com.realestate.backendrealestate.dtos.request;

import com.realestate.backendrealestate.core.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class SignUpRequestDto {
    @NotEmpty(message = "Field must not be empty.")
    private String firstName;

    @NotEmpty(message = "Field must not be empty.")
    private String lastName;

    @NotEmpty(message = "Field must not be empty.")
    private String phoneNumber;

    @NotEmpty(message = "Field must not be empty.")
    @Email(message = "It must have email's format.")
    private String email;

    @NotEmpty(message = "Field must not be empty.")
    private String password;

    @NotEmpty(message = "Field must not be empty.")
    private Role role;
}

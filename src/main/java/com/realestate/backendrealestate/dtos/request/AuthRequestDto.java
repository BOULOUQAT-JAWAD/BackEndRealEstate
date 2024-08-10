package com.realestate.backendrealestate.dtos.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthRequestDto {

    @NotEmpty(message = "Field must not be empty.")
    @Email(message = "It must have email's format.")
    private String email;

    @NotEmpty(message = "Field must not be empty.")
    private String password;
}

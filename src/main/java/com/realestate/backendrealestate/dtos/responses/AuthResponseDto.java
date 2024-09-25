package com.realestate.backendrealestate.dtos.responses;


import com.realestate.backendrealestate.core.enums.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponseDto {
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Long userId;
    private String token;
    private Role role;
}

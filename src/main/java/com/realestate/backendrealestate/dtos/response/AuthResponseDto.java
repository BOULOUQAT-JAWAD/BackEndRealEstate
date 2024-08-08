package com.realestate.backendrealestate.dtos.response;


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
}

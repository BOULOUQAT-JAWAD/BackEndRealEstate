package com.realestate.backendrealestate.entities;


import com.realestate.backendrealestate.core.enums.TokenType;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class UserToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String token;
    @Enumerated(EnumType.STRING)
    private TokenType type;
}

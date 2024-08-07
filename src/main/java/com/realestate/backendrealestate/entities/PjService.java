package com.realestate.backendrealestate.entities;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class PjService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pjServiceId;
    private String title;
    private String description;
    private double price;
    @Enumerated(EnumType.STRING)
    private PjServiceType pjServiceType;
}

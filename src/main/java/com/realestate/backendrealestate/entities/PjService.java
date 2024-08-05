package com.realestate.backendrealestate.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
    private PjServiceType pjServiceType;
}

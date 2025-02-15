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
public class ProviderPricing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long providerPricingId;
    private double servicePrice;
    private double commission;
}

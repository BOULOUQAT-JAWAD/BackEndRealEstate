package com.realestate.backendrealestate.entities;

import com.realestate.backendrealestate.core.enums.SubscriptionType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class SubscriptionTraveler {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subscriptionTravelerId;
    @Enumerated(EnumType.STRING)
    private SubscriptionType subscriptionType;
    private double totalPrice;
    private LocalDate subsDate;
    private LocalDate endSubsDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "traveler_id")
    private Traveler traveler;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "travelerOffer_id")
    private TravelerOffer travelerOffer;

}

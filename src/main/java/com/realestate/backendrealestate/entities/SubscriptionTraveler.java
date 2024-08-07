package com.realestate.backendrealestate.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

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
    private Date subsDate;
    private Date endSubsDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "traveler_id")
    private Traveler traveler;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "travelerOffer_id")
    private TravelerOffer travelerOffer;

}

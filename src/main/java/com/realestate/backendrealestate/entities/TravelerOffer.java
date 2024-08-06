package com.realestate.backendrealestate.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class TravelerOffer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long travelerOfferId;
    private TravelerOfferType travelerOfferType;
    private double price;
    private boolean hasAds;
    private boolean canComment;
    private boolean hasDiscount;
    private boolean hasFreeServices;
    private boolean vipAccess;
    private boolean renewalBonus;

    @OneToMany(mappedBy = "travelerOffer",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<SubscriptionTraveler> subscriptionTravelers;
}

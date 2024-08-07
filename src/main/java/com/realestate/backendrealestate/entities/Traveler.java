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
public class Traveler {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long travelerId;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "traveler",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<SubscriptionTraveler> subscriptionTravelers;

    @OneToMany(mappedBy = "traveler",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Reservation> reservations;

}

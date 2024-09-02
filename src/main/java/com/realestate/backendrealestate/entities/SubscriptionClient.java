package com.realestate.backendrealestate.entities;

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
public class SubscriptionClient {

    @Id
    private String subscriptionClientId;
    private double annualPrice;
    private LocalDate subsDate;
    private LocalDate endSubsDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;
}

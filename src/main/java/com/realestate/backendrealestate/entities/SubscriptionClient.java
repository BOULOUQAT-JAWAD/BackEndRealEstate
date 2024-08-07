package com.realestate.backendrealestate.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class SubscriptionClient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subscriptionClientId;
    private double annualPrice;
    private Date subsDate;
    private Date endSubsDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;
}

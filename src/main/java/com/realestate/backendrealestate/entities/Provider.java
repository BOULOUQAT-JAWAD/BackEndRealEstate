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
public class Provider {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long providerId;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "providerService_id")
    private ProviderInvoice providerInvoice;

    @OneToMany(mappedBy = "provider",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Availability> availabilities;
}

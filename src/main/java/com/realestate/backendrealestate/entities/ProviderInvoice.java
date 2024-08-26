package com.realestate.backendrealestate.entities;

import com.realestate.backendrealestate.core.enums.ProviderServiceType;
import com.realestate.backendrealestate.core.enums.ServiceType;
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
public class ProviderInvoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long providerInvoiceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id")
    private Provider provider;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id")
    private Property property;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pjService_id")
    private PjService pjService;

    @Enumerated(EnumType.STRING)
    private ServiceType serviceType;
    private LocalDate date;
    private String rating;
    private String gain;
    @Enumerated(EnumType.STRING)
    @Column(length = 255)
    private ProviderServiceType status;
    private String stripePaymentId;

}

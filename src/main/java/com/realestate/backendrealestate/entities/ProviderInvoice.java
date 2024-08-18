package com.realestate.backendrealestate.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.realestate.backendrealestate.core.enums.ProviderServiceStatus;
import com.realestate.backendrealestate.core.enums.ServiceType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

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

    @Enumerated(EnumType.STRING)
    private ServiceType serviceType;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date;
    private String rating;
    private double gain;
    @Enumerated(EnumType.STRING)
    private ProviderServiceStatus status;

    public ProviderInvoice deepCopy() {
        return ProviderInvoice.builder()
                .providerInvoiceId(this.providerInvoiceId)
                .provider(this.provider != null ? Provider.builder()
                        .providerId(this.provider.getProviderId())
                        .build() : null)
                .property(this.property != null ? Property.builder()
                        .propertyId(this.property.getPropertyId())
                        .build() : null)
                .reservation(this.reservation != null ? Reservation.builder()
                        .reservationId(this.reservation.getReservationId())
                        .build() : null)
                .serviceType(this.serviceType)
                .date(this.date)
                .rating(this.rating)
                .gain(this.gain)
                .status(this.status)
                .build();
    }
}

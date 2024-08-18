package com.realestate.backendrealestate.dtos.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.realestate.backendrealestate.core.enums.ProviderServiceStatus;
import com.realestate.backendrealestate.core.enums.ServiceType;
import com.realestate.backendrealestate.entities.Property;
import com.realestate.backendrealestate.entities.Provider;
import com.realestate.backendrealestate.entities.Reservation;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProviderInvoiceResponseDTO {
    private Long providerInvoiceId;
    private Long providerId;
    private Long propertyId;
    private Long reservationId;
    private ServiceType serviceType;
    private LocalDate date;
    private String rating;
    private double gain;
    private ProviderServiceStatus status;
}

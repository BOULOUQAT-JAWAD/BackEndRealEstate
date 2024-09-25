package com.realestate.backendrealestate.dtos.responses;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.realestate.backendrealestate.core.enums.ProviderServiceStatus;
import com.realestate.backendrealestate.core.enums.ServiceType;
import com.realestate.backendrealestate.entities.PjService;
import com.realestate.backendrealestate.entities.Property;
import com.realestate.backendrealestate.entities.Provider;
import com.realestate.backendrealestate.entities.Reservation;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class ReservationServiceResponseDto {

    private Long InvoiceId;

    private Long providerId;

    private Long reservationId;

    private PjServiceResponseDTO pjService;

    private LocalDate date;

    private String rating;

    private double gain;

    private ProviderServiceStatus status;

    private String stripePaymentId;

}

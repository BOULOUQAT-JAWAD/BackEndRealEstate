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
import lombok.Data;

import java.time.LocalDate;

@Data
public class ClientServicesDto {

    private Long providerInvoiceId;

    private Provider provider;

    private Property property;

    private PjService pjService;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date;

    private String rating;

    private double gain;

    private ProviderServiceStatus status;

    private String stripePaymentId;


}

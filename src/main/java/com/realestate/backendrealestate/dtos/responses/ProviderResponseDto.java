package com.realestate.backendrealestate.dtos.responses;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.realestate.backendrealestate.core.enums.Role;
import com.realestate.backendrealestate.entities.Availability;
import com.realestate.backendrealestate.entities.PjService;
import com.realestate.backendrealestate.entities.User;
import com.realestate.backendrealestate.entities.UserToken;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProviderResponseDto {

    private Long providerId;

    private String email;

    private Boolean activated;

    private String firstName;

    private String lastName;

    private String gender;

    private String phoneNumber;

    private PjServiceResponseDTO pjService;

    private List<ProviderInvoiceResponseDTO> providerInvoices;

//  private List<Availability> availabilities;

}

package com.realestate.backendrealestate.dtos.responses;


import com.realestate.backendrealestate.core.enums.Role;
import com.realestate.backendrealestate.entities.Property;
import com.realestate.backendrealestate.entities.SubscriptionClient;
import com.realestate.backendrealestate.entities.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ClientResponseDto {

    private Long clientId;

    private String email;

    private Boolean activated;

    private String firstName;

    private String lastName;

    private String gender;

    private String phoneNumber;

    private boolean isClientSubscribed;

    private List<SubscriptionClientResponse> subscriptionClients;
}

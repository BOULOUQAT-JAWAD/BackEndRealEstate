package com.realestate.backendrealestate.dtos.responses;


import com.realestate.backendrealestate.entities.SubscriptionTraveler;
import com.realestate.backendrealestate.services.ReservationService;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TravelerResponseDto {

    private Long travelerId;

    private String email;

    private Boolean activated;

    private String firstName;

    private String lastName;

    private String gender;

    private String phoneNumber;

    private List<SubscriptionTraveler> subscriptionTravelers;

    private List<ReservationResponseDTO> reservations;


}

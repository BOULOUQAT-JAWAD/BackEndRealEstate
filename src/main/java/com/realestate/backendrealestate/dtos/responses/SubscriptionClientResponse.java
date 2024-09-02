package com.realestate.backendrealestate.dtos.responses;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class SubscriptionClientResponse {
    private String subscriptionClientId;
    private double annualPrice;
    private LocalDate subsDate;
    private LocalDate endSubsDate;
    @JsonProperty("isClientSubscribed")
    private boolean isClientSubscribed;
}

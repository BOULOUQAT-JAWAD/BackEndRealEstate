package com.realestate.backendrealestate.dtos.requests;


import lombok.Data;

@Data
public class ReservationPaymentRequest {
    private Long propertyId;
    private Integer nightsNumber;
}

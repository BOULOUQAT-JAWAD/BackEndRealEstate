package com.realestate.backendrealestate.dtos.requests;


import lombok.Data;

import java.util.List;

@Data
public class ReservationPaymentRequest {
    private Long reservationId;
    private List<Long> pjServiceIds;
}

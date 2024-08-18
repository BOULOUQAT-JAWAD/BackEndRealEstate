package com.realestate.backendrealestate.dtos.responses;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentCardTokenResponse {
    private String cardToken;

}

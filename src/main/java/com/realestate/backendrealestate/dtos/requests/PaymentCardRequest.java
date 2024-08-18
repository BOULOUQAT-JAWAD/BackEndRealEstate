package com.realestate.backendrealestate.dtos.requests;

import lombok.Data;

@Data
public class PaymentCardRequest {

    private String cardNumber;
    private String expMonth;
    private String expYear;
    private String cvc;
}

package com.realestate.backendrealestate.dtos.requests;


import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class PaymentChargeRequest {
    private String  stripeToken;
    private String  username;
    private Double  amount;
    private Boolean success;
    private String  message;
    private String chargeId;
    private Map<String,Object> additionalInfo = new HashMap<>();

}

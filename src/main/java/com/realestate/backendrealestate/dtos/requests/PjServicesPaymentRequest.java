package com.realestate.backendrealestate.dtos.requests;


import com.realestate.backendrealestate.dtos.helper.PropertyServiceCheckout;
import com.realestate.backendrealestate.services.PropertyService;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
public class PjServicesPaymentRequest {
    private List<PropertyServiceCheckout> propertyServiceCheckouts;
}


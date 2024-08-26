package com.realestate.backendrealestate.dtos.helper;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
public class PropertyServiceCheckout {
    private Long propertyId;
    private List<Long> pjServicesIds;
}


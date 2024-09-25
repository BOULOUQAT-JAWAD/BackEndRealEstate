package com.realestate.backendrealestate.dtos.requests;

import com.realestate.backendrealestate.core.enums.PjServiceType;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PjServiceRequestDTO {
    private Long pjServiceId;
    private String title;
    private String description;
    private double price;
    private PjServiceType pjServiceType;
}

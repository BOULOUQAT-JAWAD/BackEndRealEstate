package com.realestate.backendrealestate.dtos.responses;

import com.realestate.backendrealestate.core.enums.PjServiceType;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PjServiceResponseDTO {

    private String title;
    private String description;
    private double price;
    private PjServiceType pjServiceType;
}

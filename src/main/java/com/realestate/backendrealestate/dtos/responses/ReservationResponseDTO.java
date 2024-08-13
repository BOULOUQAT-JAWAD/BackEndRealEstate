package com.realestate.backendrealestate.dtos.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.realestate.backendrealestate.core.enums.ReservationStatus;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationResponseDTO {

    private Long reservationId;
    private Long propertyId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate checkinDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate checkoutDate;
    private ReservationStatus status;
    private double price;
}

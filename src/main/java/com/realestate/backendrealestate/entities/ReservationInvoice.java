package com.realestate.backendrealestate.entities;

import com.realestate.backendrealestate.core.enums.ReservationStatus;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class ReservationInvoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationInvoiceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pjService_id")
    private PjService pjService;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

//    private double total;
//    private double discount;
//    private ReservationStatus status;
}

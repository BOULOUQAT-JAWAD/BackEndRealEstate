package com.realestate.backendrealestate.entities;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "property_invoice")
public class PropertyInvoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long propertyInvoiceId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "property_id")
    private Property property;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "pjService_id")
    private PjService pjService;

    private double total;
}

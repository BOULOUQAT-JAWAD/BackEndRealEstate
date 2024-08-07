package com.realestate.backendrealestate.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long propertyId;
    private String description;
    private String country;
    private String city;
    @Enumerated(EnumType.STRING)
    private PropertyType propertyType;
    @Enumerated(EnumType.STRING)
    private PropertyStatus propertyStatus;
//    private String locationType;
    private int numberOfRooms;
    private int numberOfPersons;
    private int surface;
    private Date occupiedFrom;
    private Date occupiedTo;
    private double pricePerNight;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private Client client;

    //List of Property Invoices
    @OneToMany(mappedBy = "property", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<PropertyInvoice> propertyInvoices;

    //List of Property Images
    @OneToMany(mappedBy = "property", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<PropertyImages> propertyImages;

}

package com.realestate.backendrealestate.dtos.requests;

import com.realestate.backendrealestate.core.enums.PropertyType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PropertyFilterDTO {

    private String description;
    private String country;
    private String city;
    private PropertyType propertyType;
    private int numberOfRooms;
    private int numberOfPersons;
    private int surface;
    private double pricePerNight;
}
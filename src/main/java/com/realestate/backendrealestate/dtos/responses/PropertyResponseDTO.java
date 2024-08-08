package com.realestate.backendrealestate.dtos.responses;

import com.realestate.backendrealestate.core.enums.PropertyStatus;
import com.realestate.backendrealestate.core.enums.PropertyType;
import com.realestate.backendrealestate.entities.PropertyImages;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class PropertyResponseDTO{
    private Long propertyId;
    private String description;
    private String country;
    private String city;
    private PropertyType propertyType;
    private PropertyStatus propertyStatus;
    private int numberOfRooms;
    private int numberOfPersons;
    private int surface;
    private Date occupiedFrom;
    private Date occupiedTo;
    private double pricePerNight;
    private boolean publish;
    private List<PropertyImages> propertyImages;
}

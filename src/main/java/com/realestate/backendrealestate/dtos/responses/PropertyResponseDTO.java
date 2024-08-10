package com.realestate.backendrealestate.dtos.responses;

import com.realestate.backendrealestate.core.enums.PropertyStatus;
import com.realestate.backendrealestate.core.enums.PropertyType;
import com.realestate.backendrealestate.entities.PropertyImages;
import com.realestate.backendrealestate.entities.PropertyPjServices;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
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

package com.realestate.backendrealestate.dtos.requests;

import com.realestate.backendrealestate.core.enums.PropertyStatus;
import com.realestate.backendrealestate.core.enums.PropertyType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PropertyRequestDTO {

    private Long propertyId;
    @NotNull(message = "Description est requise")
    private String description;
    @NotNull(message = "Pays est requise")
    private String country;
    @NotNull(message = "Ville est requise")
    private String city;
    @NotNull(message = "Type de propriété est requise")
    private PropertyType propertyType;
    @NotNull(message = "état de la propriété est requise")
    private PropertyStatus propertyStatus;
    @NotNull(message = "Nombre de chambres est requise")
    private int numberOfRooms;
    @NotNull(message = "Nombre de personnes est requise")
    private int numberOfPersons;
    @NotNull(message = "Surface est requise")
    private int surface;
    @NotNull(message = "Prix par nuit est requise")
    private double pricePerNight;
    @NotNull(message = "publish est requise")
    private boolean publish;
    private List<String> propertyImages;
}

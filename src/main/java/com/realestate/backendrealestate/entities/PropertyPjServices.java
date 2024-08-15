package com.realestate.backendrealestate.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class PropertyPjServices {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long propertyPjServicesId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id")
    @JsonIgnore
    private Property property;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pjService_id")
    @JsonIgnore
    private PjService pjService;
}

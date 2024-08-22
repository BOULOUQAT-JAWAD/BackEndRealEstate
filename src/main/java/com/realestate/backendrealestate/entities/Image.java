package com.realestate.backendrealestate.entities;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imagesId;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "property_id")
//    @JsonIgnore
//    private Property property;
    private String url;
}

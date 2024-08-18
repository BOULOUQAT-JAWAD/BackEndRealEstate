package com.realestate.backendrealestate.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Provider {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long providerId;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pjService_id")
    @JsonIgnore
    private PjService pjService;

    @OneToMany(mappedBy = "provider",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Availability> availabilities;
}

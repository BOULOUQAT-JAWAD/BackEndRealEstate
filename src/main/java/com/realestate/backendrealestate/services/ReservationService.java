package com.realestate.backendrealestate.services;

import com.realestate.backendrealestate.core.enums.ReservationStatus;
import com.realestate.backendrealestate.dtos.responses.ReservationResponseDTO;
import com.realestate.backendrealestate.entities.Property;
import com.realestate.backendrealestate.entities.Reservation;
import com.realestate.backendrealestate.mappers.ReservationMapper;
import com.realestate.backendrealestate.repositories.ReservationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
@Validated
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final PropertyService propertyService;
    private final ReservationMapper reservationMapper;

    public List<ReservationResponseDTO> getPropertyReservations(Long propertyId, LocalDate checkinDate, LocalDate checkoutDate, ReservationStatus status) {

        List<Reservation> reservations = reservationRepository.findFilteredReservations(
                propertyId,
                checkinDate,
                checkoutDate,
                status
        );

        return reservations.stream()
                .map(reservationMapper::toDto)
                .toList();
    }

    public List<ReservationResponseDTO> getClientReservations(LocalDate checkinDate, LocalDate checkoutDate, ReservationStatus status) {

        List<Property> properties = propertyService.findPropertiesByClient();
        List<Reservation> reservations = new java.util.ArrayList<>(List.of());

        properties.forEach(
                property -> {
                    reservations.addAll(
                            reservationRepository.findFilteredReservations(
                                    property.getPropertyId(),
                                    checkinDate,
                                    checkoutDate,
                                    status
                            )
                    );
                }
        );

        return reservations.stream()
                .map(reservationMapper::toDto)
                .toList();
    }

    public List<ReservationResponseDTO> getClientReservations() {

        List<Property> properties = propertyService.findPropertiesByClient();
        List<Reservation> reservations = new java.util.ArrayList<>(List.of());

        properties.forEach(
                property -> {
                    reservations.addAll(
                            reservationRepository.findByProperty(property)
                    );
                }
        );

        return reservations.stream()
                .map(reservationMapper::toDto)
                .toList();
    }
}

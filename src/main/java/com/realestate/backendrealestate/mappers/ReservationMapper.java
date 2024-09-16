package com.realestate.backendrealestate.mappers;

import com.realestate.backendrealestate.dtos.requests.ReservationRequestDTO;
import com.realestate.backendrealestate.dtos.responses.ReservationResponseDTO;
import com.realestate.backendrealestate.entities.Reservation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReservationMapper {

    @Mapping(source = "property.propertyId", target = "propertyId")
    @Mapping(source = "traveler.travelerId", target = "travelerId")
    ReservationResponseDTO toDto(Reservation reservation);

    Reservation toEntity(ReservationRequestDTO reservationRequestDTO);
}

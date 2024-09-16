package com.realestate.backendrealestate.services;

import com.realestate.backendrealestate.core.exception.NotFoundException;
import com.realestate.backendrealestate.dtos.requests.PropertyFilterDTO;
import com.realestate.backendrealestate.dtos.requests.PropertyRequestDTO;
import com.realestate.backendrealestate.dtos.responses.PropertyResponseDTO;
import com.realestate.backendrealestate.entities.*;
import com.realestate.backendrealestate.mappers.PropertyMapper;
import com.realestate.backendrealestate.repositories.PropertyRepository;
import com.realestate.backendrealestate.repositories.ReservationRepository;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDate;

@Service
@AllArgsConstructor
@Slf4j
@Validated
public class PropertyService {
    private final PropertyRepository propertyRepository;
    private final PropertyMapper propertyMapper;
    private final ReservationRepository reservationRepository;
    private final ClientService clientService;

    public PropertyResponseDTO validateProperty(long propertyId) {
        Property property = findPropertyById(propertyId);
        property.setValid(true);
        Property newProperty = propertyRepository.save(property);
        return convertPropertyToDTO(newProperty);
    }

    public PropertyResponseDTO get(long propertyId) {
        Property property = findPropertyById(propertyId);
        return convertPropertyToDTO(property);
    }

    public Boolean isPropertyAvailable(long propertyId, LocalDate checkinDate, LocalDate checkoutDate ) {
        Property property = findPropertyById(propertyId);
        List<Property> propertyList = Collections.singletonList(property);

        List<PropertyResponseDTO> filteredProperties = getFilteredProperties(propertyList, checkinDate, checkoutDate, false);

        return !filteredProperties.isEmpty();
    }

    public List<PropertyResponseDTO> getClientProperties(Boolean publish, Boolean valid) {
        log.info("getClientProperties publish {} , valid {} ", publish, valid);
        return propertyRepository.findFilteredProperties(clientService.getAuthenticatedClient(), publish, valid).stream()
                .map(this::convertPropertyToDTO)
                .collect(Collectors.toList());
    }

    private PropertyResponseDTO convertPropertyToDTO(Property property) {

        // Convert Property to PropertyResponseDTO
        PropertyResponseDTO dto = propertyMapper.toDto(property);

        // Create deep copies of the PjService entities
        /*
        Proxies: When Hibernate loads an entity lazily, it creates a proxy object. This proxy acts as a placeholder for the actual entity and intercepts method calls to load the entity data when needed.
        Problem with Proxies: When you try to serialize a proxied entity (e.g., converting it to JSON in a REST response), the serialization framework (like Jackson) may encounter issues. The proxy object may include fields or methods that are not fully initialized, leading to errors.
	    •	Jackson and Hibernate Proxy: Jackson (the JSON serializer used by Spring Boot) doesn’t know how to handle these proxy objects. When it encounters them, it may throw an exception because it can’t properly serialize the proxy’s internal state.
        Why the Deep Copy Solved the Problem:
	    •	Creating a Deep Copy: By creating a deep copy of the PjService entities, you’re effectively creating new instances of the entities without any of the proxy behavior that Hibernate introduced.
         */
//        List<PjService> pjServices = propertyPjServicesService.getPjServicesByProperty(property).stream()
//                .map(pjService -> PjService.builder()
//                        .pjServiceId(pjService.getPjServiceId())
//                        .title(pjService.getTitle())
//                        .description(pjService.getDescription())
//                        .price(pjService.getPrice())
//                        .pjServiceType(pjService.getPjServiceType())
//                        .build())
//                .collect(Collectors.toList());
//
//        // Set the deep copied PjServices in the DTO
//        dto.setPjServices(pjServices);

        return dto;
    }

    public List<PropertyResponseDTO> getClientOccupiedProperties(LocalDate checkinDate, LocalDate checkoutDate, Boolean publish, Boolean valid) {
        List<Property> properties = findPropertiesByClient(publish, valid);
        return getFilteredProperties(properties, checkinDate, checkoutDate, true);
    }

    public List<PropertyResponseDTO> getClientAvailableProperties(LocalDate checkinDate, LocalDate checkoutDate, Boolean publish, Boolean valid) {
        List<Property> properties = findPropertiesByClient(publish, valid);
        return getFilteredProperties(properties, checkinDate, checkoutDate, false);
    }

    private List<PropertyResponseDTO> getFilteredProperties(List<Property> properties, LocalDate checkinDate, LocalDate checkoutDate, boolean forOccupied) {

        List<Property> filteredProperties = properties.stream().filter(property -> {
            List<Reservation> reservations = reservationRepository.findByProperty(property);

            if (forOccupied) {
                // Check if the property has any reservation overlapping with the given date range
                return reservations.stream().anyMatch(reservation ->
                        reservation.getCheckinDate().isBefore(checkoutDate) &&
                                reservation.getCheckoutDate().isAfter(checkinDate)
                );
            } else {
                // Check if the property has no reservations overlapping with the given date range
                return reservations.stream().noneMatch(reservation ->
                        reservation.getCheckinDate().isBefore(checkoutDate) &&
                                reservation.getCheckoutDate().isAfter(checkinDate)
                );
            }
        }).toList();

        return filteredProperties.stream().map(this::convertPropertyToDTO)
                .collect(Collectors.toList());
    }

    public List<PropertyResponseDTO> getAll(PropertyFilterDTO criteria, LocalDate checkinDate, LocalDate checkoutDate,
                                            Integer minNumberOfRooms, Integer maxNumberOfRooms,
                                            Integer minNumberOfPersons, Integer maxNumberOfPersons,
                                            Integer minSurface, Integer maxSurface,
                                            Double minPricePerNight, Double maxPricePerNight) {

        String description = (criteria.getDescription() == null || criteria.getDescription().isEmpty()) ? null : "%" + criteria.getDescription().toLowerCase() + "%";
        String country = (criteria.getCountry() == null || criteria.getCountry().isEmpty()) ? null : "%" + criteria.getCountry().toLowerCase() + "%";
        String city = (criteria.getCity() == null || criteria.getCity().isEmpty()) ? null : "%" + criteria.getCity().toLowerCase() + "%";

        List<Property> properties = propertyRepository.findFilteredProperties(
                description,
                country,
                city,
                criteria.getPropertyType(),
                minNumberOfRooms,
                maxNumberOfRooms,
                minNumberOfPersons,
                maxNumberOfPersons,
                minSurface,
                maxSurface,
                minPricePerNight,
                maxPricePerNight
        ).stream().toList();

        if (checkinDate != null && checkoutDate != null) {
            return getFilteredProperties(properties, checkinDate, checkoutDate, false);
        }

        return properties.stream().map(propertyMapper::toDto).toList();
    }

    @Transactional
    public PropertyResponseDTO saveOrUpdate(@Valid PropertyRequestDTO propertyRequestDTO) {
        Property property = getPropertyForSaveOrUpdate(propertyRequestDTO);
        Client client = clientService.getAuthenticatedClient();
        property.setClient(client);
        Property savedProperty = propertyRepository.save(property);
        return get(savedProperty.getPropertyId());
    }

    public Property findPropertyById(long id) {
        return propertyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Property not found with id: " + id));
    }

    public List<Property> findPropertiesByClient() {
        Client client = clientService.getAuthenticatedClient();

        return propertyRepository.findByClient(client);
    }

    public List<Property> findPropertiesByClient(boolean publish, Boolean valid) {
        Client client = clientService.getAuthenticatedClient();

        return propertyRepository.findByClientAndPublishAndValid(client, publish, valid);
    }

    private Property getPropertyForSaveOrUpdate(PropertyRequestDTO propertyRequestDTO) {
        if (propertyRequestDTO.getPropertyId() != null) {
            Property existingProperty = findPropertyById(propertyRequestDTO.getPropertyId());
            propertyMapper.updatePropertyFromDto(propertyRequestDTO, existingProperty);
            return existingProperty;
        } else {
            return propertyMapper.toEntity(propertyRequestDTO);
        }
    }

    public double getPropertyPrice(Long propertyId){
        return get(propertyId).getPricePerNight();
    }

}

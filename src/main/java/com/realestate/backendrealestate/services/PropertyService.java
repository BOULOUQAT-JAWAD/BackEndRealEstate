package com.realestate.backendrealestate.services;

import com.realestate.backendrealestate.core.exception.NotFoundException;
import com.realestate.backendrealestate.dtos.requests.PropertyRequestDTO;
import com.realestate.backendrealestate.dtos.responses.PropertyResponseDTO;
import com.realestate.backendrealestate.entities.*;
import com.realestate.backendrealestate.mappers.PropertyMapper;
import com.realestate.backendrealestate.repositories.ClientRepository;
import com.realestate.backendrealestate.repositories.PropertyImagesRepository;
import com.realestate.backendrealestate.repositories.PropertyRepository;
import com.realestate.backendrealestate.repositories.ReservationRepository;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDate;
import java.time.ZoneId;

@Service
@AllArgsConstructor
@Slf4j
@Validated
public class PropertyService {
    private final PropertyRepository propertyRepository;
    private final PropertyImagesRepository propertyImagesRepository;
    private final PropertyMapper propertyMapper;
    private final PropertyImagesService propertyImagesService;
    private final PropertyPjServicesService propertyPjServicesService;
    private final ReservationRepository reservationRepository;
    private final ClientService clientService;

    public PropertyResponseDTO get(long propertyId) {
        Property property = findPropertyById(propertyId);
        return convertPropertyToDTO(property);
    }

    public List<PropertyResponseDTO> getClientProperties(boolean publish) {
        return findPropertiesByClient().stream()
                .filter(property -> property.isPublish() == publish)
                .map(this::convertPropertyToDTO)
                .collect(Collectors.toList());
    }

    private PropertyResponseDTO convertPropertyToDTO(Property property) {
        // Fetch the images related to the property
        List<PropertyImages> images = propertyImagesRepository.findByProperty(property);

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
        List<PjService> pjServices = propertyPjServicesService.getPjServicesByProperty(property).stream()
                .map(pjService -> PjService.builder()
                        .pjServiceId(pjService.getPjServiceId())
                        .title(pjService.getTitle())
                        .description(pjService.getDescription())
                        .price(pjService.getPrice())
                        .pjServiceType(pjService.getPjServiceType())
                        .build())
                .collect(Collectors.toList());

        // Set the deep copied PjServices in the DTO
        dto.setPjServices(pjServices);

        // Set the images in the DTO
        dto.setPropertyImages(images);

        return dto;
    }

    public List<PropertyResponseDTO> getClientOccupiedProperties(LocalDate checkinDate, LocalDate checkoutDate) {
        return getFilteredProperties(checkinDate, checkoutDate, true);
    }

    public List<PropertyResponseDTO> getClientAvailableProperties(LocalDate checkinDate, LocalDate checkoutDate) {
        return getFilteredProperties(checkinDate, checkoutDate, false);
    }

    private List<PropertyResponseDTO> getFilteredProperties(LocalDate checkinDate, LocalDate checkoutDate, boolean forOccupied) {
        List<Property> properties = findPropertiesByClient();

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

//    public List<PropertyResponseDTO> getAll() {
//        return propertyRepository.findAll().stream()
//                .map(property -> {
//                    // Fetch the images related to the property
//                    List<PropertyImages> images = propertyImagesRepository.findByProperty(property);
//
//                    // Convert Property to PropertyResponseDTO
//                    PropertyResponseDTO dto = propertyMapper.toDto(property);
//
//                    // Set the images in the DTO
//                    dto.setPropertyImages(images);
//
//                    return dto;
//                })
//                .toList();
//    }

    @Transactional
    public PropertyResponseDTO saveOrUpdate(@Valid PropertyRequestDTO propertyRequestDTO,
                                            @Nullable List<MultipartFile> images,
                                            @Nullable List<PjService> pjServices) {
        Property property = getPropertyForSaveOrUpdate(propertyRequestDTO);
        Client client = clientService.getAuthenticatedClient();
        property.setClient(client);
        Property savedProperty = propertyRepository.save(property);

        if (pjServices != null) {
            propertyPjServicesService.updatePropertyServices(savedProperty, pjServices);
        }

        if (images != null) {
            propertyImagesService.updatePropertyImages(savedProperty, images);
        }

        return get(savedProperty.getPropertyId());
    }

    @Transactional
    public void delete(long id) {
        Property property = findPropertyById(id);
        propertyImagesService.deleteImagesForProperty(property);
        propertyRepository.deleteById(property.getPropertyId());
    }

    public Property findPropertyById(long id) {
        return propertyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Property not found with id: " + id));
    }

    public List<Property> findPropertiesByClient() {
        Client client = clientService.getAuthenticatedClient();

        return propertyRepository.findByClient(client);
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

}

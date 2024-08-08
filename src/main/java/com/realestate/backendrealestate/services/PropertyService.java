package com.realestate.backendrealestate.services;

import com.realestate.backendrealestate.dtos.requests.PropertyRequestDTO;
import com.realestate.backendrealestate.dtos.responses.PropertyResponseDTO;
import com.realestate.backendrealestate.entities.Client;
import com.realestate.backendrealestate.entities.Property;
import com.realestate.backendrealestate.entities.PropertyImages;
import com.realestate.backendrealestate.mappers.PropertyMapper;
import com.realestate.backendrealestate.repositories.ClientRepository;
import com.realestate.backendrealestate.repositories.PropertyImagesRepository;
import com.realestate.backendrealestate.repositories.PropertyRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
@Validated
public class PropertyService {
    private final PropertyRepository propertyRepository;
    private final PropertyImagesRepository propertyImagesRepository;
    private final ClientRepository clientRepository;
    private final PropertyMapper propertyMapper;

    public PropertyResponseDTO get(long propertyId) {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new IllegalArgumentException("Property not found with id: " + propertyId));

        // Fetch the images related to the property
        List<PropertyImages> images = propertyImagesRepository.findAllByProperty(property);

        // Convert Property to PropertyResponseDTO
        PropertyResponseDTO dto = propertyMapper.toDto(property);

        // Set the images in the DTO
        dto.setPropertyImages(images);

        return dto;
    }

    public List<PropertyResponseDTO> getAll() {
        return propertyRepository.findAll().stream()
                .map(property -> {
                    // Fetch the images related to the property
                    List<PropertyImages> images = propertyImagesRepository.findAllByProperty(property);

                    // Convert Property to PropertyResponseDTO
                    PropertyResponseDTO dto = propertyMapper.toDto(property);

                    // Set the images in the DTO
                    dto.setPropertyImages(images);

                    return dto;
                })
                .toList();
    }

    public PropertyResponseDTO saveOrUpdate(@Valid PropertyRequestDTO propertyRequestDTO) {
        Property property;

        if (propertyRequestDTO.getPropertyId() != null) {
            // Update existing property
            property = propertyRepository.findById(propertyRequestDTO.getPropertyId())
                    .orElseThrow(() -> new IllegalArgumentException("Property not found with id: " + propertyRequestDTO.getPropertyId()));

            // Map the updated fields from DTO to the existing entity
            propertyMapper.updatePropertyFromDto(propertyRequestDTO, property);
        } else {
            // Create a new property
            property = propertyMapper.toEntity(propertyRequestDTO);
        }

        Client client = getAuthenticatedClient();
        log.info("client id: {}", client.getClientId());

        // Set the client
        property.setClient(client);

        // Save the property to the repository
        Property savedProperty = propertyRepository.save(property);

        // Return the saved entity as a DTO
        return propertyMapper.toDto(savedProperty);
    }

    public void delete(long id) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Property not found with id: " + id));
        propertyRepository.deleteById(property.getPropertyId());
    }

    public Client getAuthenticatedClient(){
        try {
            return clientRepository.findByUser(
                    AuthService.getAuthenticatedUser()
            ).orElseThrow(
                    () -> new Exception("Client not found")
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

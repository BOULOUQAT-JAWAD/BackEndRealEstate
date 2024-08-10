package com.realestate.backendrealestate.services;

import com.realestate.backendrealestate.dtos.requests.PropertyRequestDTO;
import com.realestate.backendrealestate.dtos.responses.PropertyResponseDTO;
import com.realestate.backendrealestate.entities.*;
import com.realestate.backendrealestate.mappers.PropertyMapper;
import com.realestate.backendrealestate.repositories.ClientRepository;
import com.realestate.backendrealestate.repositories.PropertyImagesRepository;
import com.realestate.backendrealestate.repositories.PropertyRepository;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
    private final ClientService clientService;

    public PropertyResponseDTO get(long propertyId) {
        Property property = findPropertyById(propertyId);

        // Fetch the images related to the property
        List<PropertyImages> images = propertyImagesRepository.findByProperty(property);

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
                    List<PropertyImages> images = propertyImagesRepository.findByProperty(property);

                    // Convert Property to PropertyResponseDTO
                    PropertyResponseDTO dto = propertyMapper.toDto(property);

                    // Set the images in the DTO
                    dto.setPropertyImages(images);

                    return dto;
                })
                .toList();
    }

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

        return propertyMapper.toDto(savedProperty);
    }

    @Transactional
    public void delete(long id) {
        Property property = findPropertyById(id);
        propertyImagesService.deleteImagesForProperty(property);
        propertyRepository.deleteById(property.getPropertyId());
    }

    private Property findPropertyById(long id) {
        return propertyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Property not found with id: " + id));
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

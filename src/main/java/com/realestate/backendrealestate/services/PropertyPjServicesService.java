package com.realestate.backendrealestate.services;
import com.realestate.backendrealestate.entities.PjService;
import com.realestate.backendrealestate.entities.Property;
import com.realestate.backendrealestate.entities.PropertyPjServices;
import com.realestate.backendrealestate.repositories.PjServiceRepository;
import com.realestate.backendrealestate.repositories.PropertyPjServicesRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
@Validated
public class PropertyPjServicesService {

    private final PropertyPjServicesRepository propertyPjServicesRepository;
    private final PjServiceRepository pjServiceRepository;
    private final PjServicesService pjServicesService;

    @Transactional
    public void updatePropertyServices(Property property, List<PjService> pjServices) {
        List<PropertyPjServices> existingServices = propertyPjServicesRepository.findAllByProperty(property);

        if (existingServices != null) {
            // Delete services that are no longer associated
            for (PropertyPjServices existingService : existingServices) {
                if (!pjServices.contains(existingService.getPjService())) {
                    propertyPjServicesRepository.delete(existingService);
                }
            }
        }

        // Add new services that are not already associated
        for (PjService pjService : pjServices) {
            boolean serviceExists = existingServices != null && existingServices.stream()
                    .anyMatch(existingService -> existingService.getPjService().equals(pjService));

            if (!serviceExists) {
                // Ensure pjService is persisted before associating it
                PjService existingPjService = pjServiceRepository.findById(pjService.getPjServiceId())
                        .orElseGet(() -> pjServiceRepository.save(pjService));

                PropertyPjServices propertyPjServices = new PropertyPjServices();
                propertyPjServices.setProperty(property);
                propertyPjServices.setPjService(existingPjService);
                propertyPjServicesRepository.save(propertyPjServices);
            }
        }
    }

    public List<PjService> getPjServicesByProperty(Property property){
        List<PropertyPjServices> propertyPjServices = propertyPjServicesRepository.findAllByProperty(property);
        List<PjService> pjServices = new java.util.ArrayList<>(List.of());

        propertyPjServices.forEach(
                propertyPjService -> {
                    pjServices.add(pjServicesService.get(propertyPjService.getPjService().getPjServiceId()));
                }
        );
        return pjServices;
    }
}

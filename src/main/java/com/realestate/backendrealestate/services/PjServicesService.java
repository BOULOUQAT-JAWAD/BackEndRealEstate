package com.realestate.backendrealestate.services;

import com.realestate.backendrealestate.core.enums.PjServiceType;
import com.realestate.backendrealestate.core.exception.NotFoundException;
import com.realestate.backendrealestate.dtos.responses.PjServiceResponseDTO;
import com.realestate.backendrealestate.entities.PjService;
import com.realestate.backendrealestate.mappers.PjServiceMapper;
import com.realestate.backendrealestate.repositories.PjServiceRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
@Validated
public class PjServicesService {

    private final PjServiceRepository pjServiceRepository;
    private final PjServiceMapper pjServiceMapper;

    public List<PjServiceResponseDTO> getPjServicesByType(PjServiceType type) {
        log.info("Fetching PjServices of type: {}", type);
        return pjServiceRepository.findByPjServiceType(type).stream()
                .map(pjServiceMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<PjServiceResponseDTO> getPjServicesForClient() {
        return getPjServicesByType(PjServiceType.Client);
    }

    public List<PjServiceResponseDTO> getPjServicesForVoyageur() {
        return getPjServicesByType(PjServiceType.Voyageur);
    }

    public PjService get(Long id){
        return pjServiceRepository.findById(
                id
        ).orElseThrow(
                () -> new NotFoundException("PjService Not Found")
        );
    }

}

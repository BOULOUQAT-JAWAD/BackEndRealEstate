package com.realestate.backendrealestate.services;

import com.realestate.backendrealestate.dtos.responses.PjServiceResponseDTO;
import com.realestate.backendrealestate.mappers.PjServiceMapper;
import com.realestate.backendrealestate.repositories.PjServiceRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
@Validated
public class PjServicesService {

    private final PjServiceRepository pjServiceRepository;
    private final PjServiceMapper pjServiceMapper;

    public List<PjServiceResponseDTO> getAll() {
        // Convert Property to PropertyResponseDTO
        return pjServiceRepository.findAll().stream()
                .map(pjServiceMapper::toDto)
                .toList();
    }

}

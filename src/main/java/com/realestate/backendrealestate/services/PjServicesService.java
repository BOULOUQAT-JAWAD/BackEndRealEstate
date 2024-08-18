package com.realestate.backendrealestate.services;

import com.realestate.backendrealestate.core.enums.PjServiceType;
import com.realestate.backendrealestate.dtos.responses.PjServiceResponseDTO;
import com.realestate.backendrealestate.entities.PjService;
import com.realestate.backendrealestate.mappers.PjServiceMapper;
import com.realestate.backendrealestate.repositories.PjServiceRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Validated
public class PjServicesService {

    private final PjServiceRepository pjServiceRepository;
    private final PjServiceMapper pjServiceMapper;

    @Value("${PjPricing.clientAnnualSubscription}")
    private double defaultClientAnnualSubscription;

    public List<PjServiceResponseDTO> getAll() {
        // Convert Property to PropertyResponseDTO
        return pjServiceRepository.findAll().stream()
                .map(pjServiceMapper::toDto)
                .toList();
    }

    public double getAnnualClientSubscriptionPrice(){
        Optional<PjService> pjServiceOptional =
                pjServiceRepository.findByPjServiceType(PjServiceType.CLIENT_ANNUAL_SUBSCRIPTION);
        if (pjServiceOptional.isEmpty()){
             pjServiceRepository
                    .save(PjService.builder()
                            .price(defaultClientAnnualSubscription)
                            .pjServiceType(PjServiceType.CLIENT_ANNUAL_SUBSCRIPTION)
                            .title("Client Annual Subscription")
                            .description("Client Annual Subscription")
                            .build());
             return defaultClientAnnualSubscription;
        }
        return pjServiceOptional.get().getPrice();
    }
}

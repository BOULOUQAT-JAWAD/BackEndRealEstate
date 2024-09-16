package com.realestate.backendrealestate.services;

import com.realestate.backendrealestate.core.enums.PjServiceType;
import com.realestate.backendrealestate.core.exception.NotFoundException;
import com.realestate.backendrealestate.dtos.responses.PjServiceResponseDTO;
import com.realestate.backendrealestate.entities.PjService;
import com.realestate.backendrealestate.mappers.PjServiceMapper;
import com.realestate.backendrealestate.repositories.PjServiceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.stream.Collectors;

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
        List<PjService> pjServices =
                pjServiceRepository.findByPjServiceType(PjServiceType.CLIENT_ANNUAL_SUBSCRIPTION);
        if (pjServices.isEmpty()){
            pjServiceRepository
                    .save(PjService.builder()
                            .price(defaultClientAnnualSubscription)
                            .pjServiceType(PjServiceType.CLIENT_ANNUAL_SUBSCRIPTION)
                            .title("Client Annual Subscription")
                            .description("Client Annual Subscription")
                            .build());
            return defaultClientAnnualSubscription;
        }
        return pjServices.get(0).getPrice();
    }

    public double getPjServicePrice(Long pjServiceId){
        return getPjServiceById(pjServiceId).getPrice();
    }
    public PjService getPjServiceById(Long pjServiceId){
        PjService pjService = pjServiceRepository.findById(pjServiceId).orElseThrow(()-> new NotFoundException("PjService with id "+pjServiceId+" does not exist"));
        log.info(String.valueOf(pjService));
        return pjService;
    }

    public List<PjServiceResponseDTO> getPjServicesByType(PjServiceType type) {
        log.info("Fetching PjServices of type: {}", type);
        return pjServiceRepository.findByPjServiceType(type).stream()
                .map(pjServiceMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<PjServiceResponseDTO> getPjServicesForClient() {
        return getPjServicesByType(PjServiceType.CLIENT);
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

package com.realestate.backendrealestate.services;

import com.realestate.backendrealestate.dtos.responses.DefaultResponseDto;
import com.realestate.backendrealestate.dtos.responses.PjServiceResponseDTO;
import com.realestate.backendrealestate.dtos.responses.ProviderInvoiceResponseDTO;
import com.realestate.backendrealestate.dtos.responses.ProviderResponseDto;
import com.realestate.backendrealestate.entities.Provider;
import com.realestate.backendrealestate.entities.ProviderInvoice;
import com.realestate.backendrealestate.entities.Traveler;
import com.realestate.backendrealestate.entities.User;
import com.realestate.backendrealestate.repositories.PropertyRepository;
import com.realestate.backendrealestate.repositories.ProviderInvoiceRepository;
import com.realestate.backendrealestate.repositories.ProviderRepository;
import com.realestate.backendrealestate.repositories.TravelerRepository;
import com.realestate.backendrealestate.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
@Validated
public class ProviderService {

    private final ProviderRepository providerRepository;
    private final ProviderInvoiceService providerInvoiceService;
    private final UserRepository userRepository;

    public void saveProvider(Provider provider){
        providerRepository.save(provider);
    }

    public List<ProviderResponseDto> getAllProviders() {
        List<Provider> providerList = providerRepository.findAll();
        List<ProviderResponseDto> providerResponseDtos = new ArrayList<>();
        for(Provider provider : providerList){
            providerResponseDtos.add(ProviderResponseDto.builder()
                            .providerId(provider.getProviderId())
                            .email(provider.getUser().getEmail())
                            .firstName(provider.getUser().getFirstName())
                            .lastName(provider.getUser().getLastName())
                            .phoneNumber(provider.getUser().getPhoneNumber())
                            .activated(provider.getUser().getActivated())
                            .pjService(PjServiceResponseDTO.builder()
                                    .pjServiceId(provider.getPjService().getPjServiceId())
                                    .description(provider.getPjService().getDescription())
                                    .price(provider.getPjService().getPrice())
                                    .title(provider.getPjService().getTitle())
                                    .pjServiceType(provider.getPjService().getPjServiceType())
                                    .build())
                            .providerInvoices(getProviderInvoicesDto(provider.getProviderId()))

                    .build());
        }
        return providerResponseDtos;
    }

    private List<ProviderInvoiceResponseDTO> getProviderInvoicesDto(Long providerId) {
        List<ProviderInvoice> providerInvoiceList = providerInvoiceService.getProviderInvoicesByProviderId(providerId);
        List<ProviderInvoiceResponseDTO> providerInvoiceResponseDTOS = new ArrayList<>();

        for (ProviderInvoice providerInvoice : providerInvoiceList ){
            providerInvoiceResponseDTOS.add(
                    ProviderInvoiceResponseDTO.builder()
                            .providerInvoiceId(providerInvoice.getProviderInvoiceId())
                            .date(providerInvoice.getDate())
                            .gain(providerInvoice.getGain())
                            .serviceType(providerInvoice.getServiceType())
                            .status(providerInvoice.getStatus())
                            .propertyId(providerInvoice.getProperty() != null ? providerInvoice.getProperty().getPropertyId() : null)
                            .reservationId(providerInvoice.getReservation() != null ? providerInvoice.getReservation().getReservationId() : null)
                            .rating(providerInvoice.getRating())
                            .build()
            );
        }

        return providerInvoiceResponseDTOS;
    }

    public DefaultResponseDto changeProviderActivation(Long providerId) {
        User providerUser = providerRepository.findById(providerId).get().getUser();
        if (providerUser.getActivated()){
            providerUser.setActivated(false);
        }
        else {
            providerUser.setActivated(true);
        }
        userRepository.save(providerUser);
        return DefaultResponseDto.builder()
                .message("provider activation changed")
                .time(new Date())
                .status(HttpStatus.OK.getReasonPhrase())
                .build();
    }
}

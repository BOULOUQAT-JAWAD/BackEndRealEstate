package com.realestate.backendrealestate.services;


import com.realestate.backendrealestate.core.exception.BadRequestException;
import com.realestate.backendrealestate.dtos.responses.DefaultResponseDto;
import com.realestate.backendrealestate.entities.Client;
import com.realestate.backendrealestate.entities.SubscriptionClient;
import com.realestate.backendrealestate.repositories.SubscriptionClientRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@Slf4j
@RequiredArgsConstructor
public class SubscriptionClientService {

    private final SubscriptionClientRepository subscriptionClientRepository;
    private final ClientService clientService;
    private final PjServicesService pjServicesService;


    @Transactional
    public DefaultResponseDto subscribeClient() {
        Client client = clientService.getAuthenticatedClient();
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(365);
        if (isSubscribed(client)){
            throw new BadRequestException("Client is already subscribed");
        }
        log.info("Subscribing to client " + client.getClientId() + " with startDate " + startDate);
        double subscriptionPrice = pjServicesService.getAnnualClientSubscriptionPrice();
        SubscriptionClient subscriptionClient = SubscriptionClient.builder()
                .client(client)
                .annualPrice(subscriptionPrice)
                .subsDate(startDate)
                .endSubsDate(endDate)
                .build();
        subscriptionClientRepository.save(subscriptionClient);


        return null;
    }

    public boolean isSubscribed(Client client) {
        return client.getSubscriptionClients()
                .stream()
                .anyMatch(subscriptionClient -> subscriptionClient.getEndSubsDate().isAfter(LocalDate.now()));
    }
}

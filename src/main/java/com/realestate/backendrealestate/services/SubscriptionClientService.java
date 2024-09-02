package com.realestate.backendrealestate.services;


import com.realestate.backendrealestate.core.exception.BadRequestException;
import com.realestate.backendrealestate.core.exception.RealEstateGlobalException;
import com.realestate.backendrealestate.dtos.responses.SubscriptionClientResponse;
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
    public void subscribeClient(String clientEmail, String subscriptionId) {
        Client client = clientService.getClientByEmail(clientEmail);
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(365);
        if (isSubscribed(client)){
            throw new BadRequestException("Client is already subscribed");
        }
        log.info("Subscribing to client " + client.getClientId() + " with startDate " + startDate);
        double subscriptionPrice = pjServicesService.getAnnualClientSubscriptionPrice();
        SubscriptionClient subscriptionClient = SubscriptionClient.builder()
                .subscriptionClientId(subscriptionId)
                .client(client)
                .annualPrice(subscriptionPrice)
                .subsDate(startDate)
                .endSubsDate(endDate)
                .build();
        subscriptionClientRepository.save(subscriptionClient);
    }

    public boolean isSubscribed(Client client) {
        return client.getSubscriptionClients()
                .stream()
                .anyMatch(subscriptionClient -> subscriptionClient.getEndSubsDate().isAfter(LocalDate.now()));
    }

    public SubscriptionClientResponse getClientSubscription(Long userId) {
        Client client = clientService.getClientByUserId(userId);
        if (!isSubscribed(client)){
            return SubscriptionClientResponse.builder()
                    .isClientSubscribed(false)
                    .build();
        }
        SubscriptionClient subscriptionClient = getSubscription(client.getClientId());
        return SubscriptionClientResponse.builder()
                .isClientSubscribed(true)
                .annualPrice(subscriptionClient.getAnnualPrice())
                .endSubsDate(subscriptionClient.getEndSubsDate())
                .subsDate(subscriptionClient.getSubsDate())
                .subscriptionClientId(subscriptionClient.getSubscriptionClientId())
                .build();
    }

    public SubscriptionClient getSubscription(Long clientId){
        return subscriptionClientRepository.findByClientClientId(clientId).orElseThrow(() -> {log.error("client subscription not found");
            return new RealEstateGlobalException("client subscription not found");});
    }
}

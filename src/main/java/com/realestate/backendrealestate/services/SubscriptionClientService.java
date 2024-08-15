package com.realestate.backendrealestate.services;


import com.realestate.backendrealestate.dtos.requests.SubsClientRequest;
import com.realestate.backendrealestate.entities.Client;
import com.realestate.backendrealestate.repositories.SubscriptionClientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.stream.Stream;

@Service
@Slf4j
@RequiredArgsConstructor
public class SubscriptionClientService {
    private final SubscriptionClientRepository subscriptionClientRepository;
    private final ClientService clientService;

    public void subscribeClient(SubsClientRequest subsClientRequest) {
        Client client = clientService.getAuthenticatedClient();
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(365);

    }

    public boolean isSubscribed(Client client) {
        return client.getSubscriptionClients().stream().map(subscriptionClient -> {
            if (subscriptionClient.getEndSubsDate().isAfter(LocalDate.now())){
                return true;
            }
            return false;
        }).isParallel();
    }
}

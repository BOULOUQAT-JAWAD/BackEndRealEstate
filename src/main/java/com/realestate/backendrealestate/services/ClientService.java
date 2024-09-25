package com.realestate.backendrealestate.services;

import com.realestate.backendrealestate.core.exception.NotFoundException;
import com.realestate.backendrealestate.dtos.responses.ClientResponseDto;
import com.realestate.backendrealestate.dtos.responses.DefaultResponseDto;
import com.realestate.backendrealestate.dtos.responses.SubscriptionClientResponse;
import com.realestate.backendrealestate.entities.Client;
import com.realestate.backendrealestate.entities.SubscriptionClient;
import com.realestate.backendrealestate.entities.User;
import com.realestate.backendrealestate.repositories.ClientRepository;
import com.realestate.backendrealestate.repositories.UserRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
@Validated
public class ClientService {

    private final ClientRepository clientRepository;

    private final SubscriptionClientService subscriptionClientService;

    private final SecurityService securityService;
    private final UserRepository userRepository;

    public ClientService(ClientRepository clientRepository, @Lazy SubscriptionClientService subscriptionClientService, @Lazy SecurityService securityService, UserRepository userRepository) {
        this.clientRepository = clientRepository;
        this.subscriptionClientService = subscriptionClientService;
        this.securityService = securityService;
        this.userRepository = userRepository;
    }

    public Client getAuthenticatedClient(){
        try {
            return clientRepository.findByUser(
                    securityService.getAuthenticatedUser()
            ).orElseThrow(
                    () -> new NotFoundException("Client not found")
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void saveClient(Client client){
        clientRepository.save(client);
    }

    public Client getClientByEmail(String email){
        return clientRepository.findByUserEmail(email).orElseThrow( () -> new NotFoundException("Client not found"));
    }

    public Client getClientByUserId(Long userId) {
        return clientRepository.findByUserUserId(userId).orElseThrow( () -> new NotFoundException("Client not found"));
    }

    public  List<ClientResponseDto> getAllClients() {

        List<Client> clientList = clientRepository.findAll();

        List<ClientResponseDto> clientResponseList = new ArrayList<>();

        for (Client client : clientList){
            clientResponseList.add(
                    ClientResponseDto.builder()
                            .clientId(client.getClientId())
                            .activated(client.getUser().getActivated())
                            .subscriptionClients(
                                    prepareSubscribesClientResponse(client.getSubscriptionClients())
                            )
                            .isClientSubscribed(subscriptionClientService.isSubscribed(client))
                            .email(client.getUser().getEmail())
                            .firstName(client.getUser().getFirstName())
                            .lastName(client.getUser().getLastName())
                            .gender(client.getUser().getGender())
                            .phoneNumber(client.getUser().getPhoneNumber())
                            .build()
            );
        }
        return clientResponseList;
    }

    private List<SubscriptionClientResponse> prepareSubscribesClientResponse(List<SubscriptionClient> subscriptionClients) {
        List<SubscriptionClientResponse> subscriptionClientResponseList = new ArrayList<>();
        for (SubscriptionClient subscriptionClient : subscriptionClients){
            subscriptionClientResponseList.add(
                    SubscriptionClientResponse.builder()
                            .subscriptionClientId(subscriptionClient.getSubscriptionClientId())
                            .annualPrice(subscriptionClient.getAnnualPrice())
                            .subsDate(subscriptionClient.getSubsDate())
                            .endSubsDate(subscriptionClient.getEndSubsDate())
                            .build()
            );
        }
        return subscriptionClientResponseList;
    }

    public DefaultResponseDto changeClientActivation(Long clientId) {
        User clientUser = clientRepository.findById(clientId).get().getUser();
        if (clientUser.getActivated()){
            clientUser.setActivated(false);
        }
        else {
            clientUser.setActivated(true);
        }
        userRepository.save(clientUser);
        return DefaultResponseDto.builder()
                .message("client activation changed")
                .time(new Date())
                .status(HttpStatus.OK.getReasonPhrase())
                .build();
    }
}

package com.realestate.backendrealestate.services;

import com.realestate.backendrealestate.core.exception.NotFoundException;
import com.realestate.backendrealestate.core.exception.RealEstateGlobalException;
import com.realestate.backendrealestate.entities.Client;
import com.realestate.backendrealestate.repositories.ClientRepository;
import com.realestate.backendrealestate.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@AllArgsConstructor
@Slf4j
@Validated
public class ClientService {

    private final ClientRepository clientRepository;

    @Lazy
    private final SecurityService securityService;

    public Client getAuthenticatedClient(){
            return clientRepository.findByUser(
                    securityService.getAuthenticatedUser()
            ).orElseThrow(
                    () -> new NotFoundException("Client not found")
            );
    }

    public void saveClient(Client client){
        clientRepository.save(client);
    }
}

package com.realestate.backendrealestate.services;

import com.realestate.backendrealestate.entities.Client;
import com.realestate.backendrealestate.repositories.ClientRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@AllArgsConstructor
@Slf4j
@Validated
public class ClientService {

    private final ClientRepository clientRepository;

    public Client getAuthenticatedClient(){
        try {
            return clientRepository.findByUser(
                    AuthService.getAuthenticatedUser()
            ).orElseThrow(
                    () -> new Exception("Client not found")
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

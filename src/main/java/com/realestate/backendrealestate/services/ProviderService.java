package com.realestate.backendrealestate.services;

import com.realestate.backendrealestate.entities.Provider;
import com.realestate.backendrealestate.entities.Traveler;
import com.realestate.backendrealestate.repositories.PropertyRepository;
import com.realestate.backendrealestate.repositories.ProviderRepository;
import com.realestate.backendrealestate.repositories.TravelerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@AllArgsConstructor
@Slf4j
@Validated
public class ProviderService {

    private final ProviderRepository providerRepository;

    public void saveProvider(Provider provider){
        providerRepository.save(provider);
    }
}

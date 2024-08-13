package com.realestate.backendrealestate.services;


import com.realestate.backendrealestate.entities.Traveler;
import com.realestate.backendrealestate.repositories.TravelerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@AllArgsConstructor
@Slf4j
@Validated
public class TravelerService {

    private final TravelerRepository travelerRepository;

    public void saveTraveler(Traveler traveler){
        travelerRepository.save(traveler);
    }




}

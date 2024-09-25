package com.realestate.backendrealestate.controllers;


import com.realestate.backendrealestate.dtos.responses.ClientResponseDto;
import com.realestate.backendrealestate.dtos.responses.DefaultResponseDto;
import com.realestate.backendrealestate.dtos.responses.TravelerResponseDto;
import com.realestate.backendrealestate.services.TravelerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/admin/traveler")
@AllArgsConstructor
public class TravelerController {

    private final TravelerService travelerService;

    @GetMapping
    public ResponseEntity<List<TravelerResponseDto>> getAllTravelers(){
        return ResponseEntity.ok(travelerService.getAllTravelers());
    }

    @PutMapping("/changestatus/{travelerId}")
    public ResponseEntity<DefaultResponseDto> changeClientActivation(@PathVariable Long travelerId){
        return ResponseEntity.ok(travelerService.changeTravelerActivation(travelerId));
    }
}

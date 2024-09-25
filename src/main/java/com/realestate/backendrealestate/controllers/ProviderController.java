package com.realestate.backendrealestate.controllers;


import com.realestate.backendrealestate.dtos.responses.ClientResponseDto;
import com.realestate.backendrealestate.dtos.responses.DefaultResponseDto;
import com.realestate.backendrealestate.dtos.responses.ProviderResponseDto;
import com.realestate.backendrealestate.services.ClientService;
import com.realestate.backendrealestate.services.ProviderService;
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
@RequestMapping("/api/admin/provider")
@AllArgsConstructor
public class ProviderController {

    private ProviderService providerService;

    @GetMapping
    public ResponseEntity<List<ProviderResponseDto>> getAllProviders(){
        return ResponseEntity.ok(providerService.getAllProviders());
    }

    @PutMapping("/changestatus/{providerId}")
    public ResponseEntity<DefaultResponseDto> changeClientActivation(@PathVariable Long providerId){
        return ResponseEntity.ok(providerService.changeProviderActivation(providerId));
    }

}

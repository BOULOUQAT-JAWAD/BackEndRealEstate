package com.realestate.backendrealestate.controllers;

import com.realestate.backendrealestate.dtos.responses.PjServiceResponseDTO;
import com.realestate.backendrealestate.services.PjServicesService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/pjServices")
@AllArgsConstructor
public class PjServicesController {

    private final PjServicesService pjServicesService;

    @GetMapping("/client")
    public ResponseEntity<List<PjServiceResponseDTO>> getPjServicesForClient() {
        log.info("Getting PjServices For Client");
        return ResponseEntity.ok(pjServicesService.getPjServicesForClient());
    }


    @GetMapping("/voyageur")
    public ResponseEntity<List<PjServiceResponseDTO>> getPjServicesForVoyageur() {
        log.info("Getting PjServices For Voyageur");
        return ResponseEntity.ok(pjServicesService.getPjServicesForVoyageur());
    }
}

package com.realestate.backendrealestate.controllers;

import com.realestate.backendrealestate.dtos.requests.PjServiceRequestDTO;
import com.realestate.backendrealestate.dtos.responses.PjServiceResponseDTO;
import com.realestate.backendrealestate.services.PjServicesService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/getAll")
    public ResponseEntity<List<PjServiceResponseDTO>> getAll() {
        log.info("Getting All PjServices");
        return ResponseEntity.ok(pjServicesService.getAll());
    }

    @GetMapping("/{pjServiceId}")
    public ResponseEntity<PjServiceResponseDTO> get(
            @PathVariable long pjServiceId
    ) {
        log.info("Getting one PjServices {}", pjServiceId);
        return ResponseEntity.ok(pjServicesService.getOne(pjServiceId));
    }

    @PostMapping
    public ResponseEntity<PjServiceResponseDTO> saveOrUpdate(
            @RequestBody PjServiceRequestDTO pjServiceRequestDTO
            ) {
        log.info("Saving or updating PjService");
        return ResponseEntity.ok(pjServicesService.saveOrUpdate(pjServiceRequestDTO));
    }
}

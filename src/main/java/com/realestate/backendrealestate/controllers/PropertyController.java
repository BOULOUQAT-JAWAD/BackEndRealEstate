package com.realestate.backendrealestate.controllers;

import com.realestate.backendrealestate.dtos.requests.PropertyRequestDTO;
import com.realestate.backendrealestate.dtos.responses.PropertyResponseDTO;
import com.realestate.backendrealestate.services.PropertyService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

@Slf4j
@RestController
@RequestMapping("/api/properties")
@AllArgsConstructor
public class PropertyController {

    private final PropertyService propertyService;

    @PostMapping()
    public ResponseEntity<PropertyResponseDTO> saveOrUpdate(@RequestBody PropertyRequestDTO propertyRequestDTO) {
        log.info("Save Or Update Property");
        return ResponseEntity.ok(propertyService.saveOrUpdate(propertyRequestDTO));
    }

    @GetMapping
    public ResponseEntity<List<PropertyResponseDTO>> getClientProperties(
            @RequestParam(value = "publish", required = false) Boolean publish,
            @RequestParam(value = "valid", required = false) Boolean valid
    ) {
        log.info("Getting Properties");
        return ResponseEntity.ok(propertyService.getClientProperties(publish, valid));
    }

    @GetMapping("/{propertyId}")
    public ResponseEntity<PropertyResponseDTO> get(@PathVariable Long propertyId) {
        log.info("Getting property with id: {}", propertyId);
        return ResponseEntity.ok(propertyService.get(propertyId));
    }

    // Only Pj Admin can call it
    @GetMapping("/{propertyId}/validate")
    public ResponseEntity<PropertyResponseDTO> validateProperty(@PathVariable Long propertyId) {
        return ResponseEntity.ok(propertyService.validateProperty(propertyId));
    }

    @GetMapping("/occupied")
    public ResponseEntity<List<PropertyResponseDTO>> getClientOccupiedProperties(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam(value = "publish", required = false) Boolean publish,
            @RequestParam(value = "valid", required = false) Boolean valid) {

        log.info("Getting Client's occupied properties : between {} and {} ", startDate, endDate);
        return ResponseEntity.ok(propertyService.getClientOccupiedProperties(startDate, endDate, publish, valid));
    }

    @GetMapping("/available")
    public ResponseEntity<List<PropertyResponseDTO>> getClientAvailableProperties(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam(value = "publish", required = false) Boolean publish,
            @RequestParam(value = "valid", required = false) Boolean valid) {

        log.info("Getting Client's available properties : between {} and {} ", startDate, endDate);
        return ResponseEntity.ok(propertyService.getClientAvailableProperties(startDate, endDate, publish, valid));
    }

}

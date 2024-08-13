package com.realestate.backendrealestate.controllers;

import com.realestate.backendrealestate.core.enums.ReservationStatus;
import com.realestate.backendrealestate.dtos.requests.PropertyRequestDTO;
import com.realestate.backendrealestate.dtos.responses.PropertyResponseDTO;
import com.realestate.backendrealestate.dtos.responses.ReservationResponseDTO;
import com.realestate.backendrealestate.dtos.responses.ResponseDto;
import com.realestate.backendrealestate.entities.PjService;
import com.realestate.backendrealestate.services.PropertyService;
import com.realestate.backendrealestate.services.ReservationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;
import org.springframework.format.annotation.DateTimeFormat;

@Slf4j
@RestController
@RequestMapping("/api/properties")
@AllArgsConstructor
public class PropertyController {

    private final PropertyService propertyService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PropertyResponseDTO> saveOrUpdate(@RequestPart("property") PropertyRequestDTO propertyRequestDTO,
                                                            @RequestPart(value = "images", required = false) List<MultipartFile> images) {
        log.info("Save Or Update Property");
        return ResponseEntity.ok(propertyService.saveOrUpdate(propertyRequestDTO, images, propertyRequestDTO.getPjServices()));
    }

    @GetMapping
    public ResponseEntity<List<PropertyResponseDTO>> getClientProperties() {
        log.info("Getting Properties");
        return ResponseEntity.ok(propertyService.getClientProperties());
    }

    @GetMapping("/{propertyId}")
    public ResponseEntity<PropertyResponseDTO> get(@PathVariable Long propertyId) {
        log.info("Getting property with id: {}", propertyId);
        return ResponseEntity.ok(propertyService.get(propertyId));
    }

    @GetMapping("/occupied")
    public ResponseEntity<List<PropertyResponseDTO>> getClientOccupiedProperties(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {

        log.info("Getting Client's occupied properties : between {} and {} ", startDate, endDate);
        return ResponseEntity.ok(propertyService.getClientOccupiedProperties(startDate, endDate));
    }

    @GetMapping("/available")
    public ResponseEntity<List<PropertyResponseDTO>> getClientAvailableProperties(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {

        log.info("Getting Client's available properties : between {} and {} ", startDate, endDate);
        return ResponseEntity.ok(propertyService.getClientAvailableProperties(startDate, endDate));
    }

    @DeleteMapping("/{propertyId}")
    public void delete(@PathVariable Long propertyId) {
        log.info("Deleting Property with id: {}", propertyId);
        propertyService.delete(propertyId);
    }
}

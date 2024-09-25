package com.realestate.backendrealestate.controllers;

import com.realestate.backendrealestate.dtos.requests.PropertyFilterDTO;
import com.realestate.backendrealestate.dtos.requests.PropertyRequestDTO;
import com.realestate.backendrealestate.dtos.responses.PropertyResponseDTO;
import com.realestate.backendrealestate.services.PropertyService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @GetMapping("/home/getAll")
    public ResponseEntity<List<PropertyResponseDTO>> getAll(
            @ModelAttribute PropertyFilterDTO criteria,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate checkinDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate checkoutDate,
            @RequestParam(required = false) Integer minNumberOfRooms,
            @RequestParam(required = false) Integer maxNumberOfRooms,
            @RequestParam(required = false) Integer minNumberOfPersons,
            @RequestParam(required = false) Integer maxNumberOfPersons,
            @RequestParam(required = false) Integer minSurface,
            @RequestParam(required = false) Integer maxSurface,
            @RequestParam(required = false) Double minPricePerNight,
            @RequestParam(required = false) Double maxPricePerNight
    ) {
        log.info("Getting All Properties");
        if(criteria == null){
            return ResponseEntity.ok(propertyService.getAll(
                    new PropertyFilterDTO(),
                    checkinDate, checkoutDate,
                    minNumberOfRooms, maxNumberOfRooms,
                    minNumberOfPersons, maxNumberOfPersons,
                    minSurface, maxSurface,
                    minPricePerNight, maxPricePerNight));
        }
        return ResponseEntity.ok(propertyService.getAll(
                criteria,
                checkinDate, checkoutDate,
                minNumberOfRooms, maxNumberOfRooms,
                minNumberOfPersons, maxNumberOfPersons,
                minSurface, maxSurface,
                minPricePerNight, maxPricePerNight));
    }


    @GetMapping
    public ResponseEntity<List<PropertyResponseDTO>> getClientProperties(
            @RequestParam(value = "publish", required = false) Boolean publish,
            @RequestParam(value = "valid", required = false) Boolean valid
    ) {
        log.info("Getting Properties");
        return ResponseEntity.ok(propertyService.getClientProperties(publish, valid));
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<PropertyResponseDTO>> getAll(
            @RequestParam(value = "valid", required = false) Boolean valid
    ) {
        log.info("Getting Properties");
        return ResponseEntity.ok(propertyService.findAll(valid));
    }

    @GetMapping("/{propertyId}")
    public ResponseEntity<PropertyResponseDTO> get(@PathVariable Long propertyId) {
        log.info("Getting property with id: {}", propertyId);
        return ResponseEntity.ok(propertyService.get(propertyId));
    }

    @GetMapping("/home/{propertyId}")
    public ResponseEntity<PropertyResponseDTO> getOne(@PathVariable Long propertyId) {
        log.info("Getting property with id: {}", propertyId);
        return ResponseEntity.ok(propertyService.get(propertyId));
    }

    @GetMapping("/home/{propertyId}/available")
    public ResponseEntity<Boolean> isPropertyAvailable(
            @PathVariable Long propertyId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate checkinDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate checkoutDate
    ) {
        log.info("check availability property with id: {}", propertyId);
        return ResponseEntity.ok(propertyService.isPropertyAvailable(propertyId, checkinDate, checkoutDate));
    }

    // Only Pj Admin can call it
    @GetMapping("/{propertyId}/validate")
    public ResponseEntity<PropertyResponseDTO> validateProperty(@PathVariable Long propertyId) {
        return ResponseEntity.ok(propertyService.validateProperty(propertyId));
    }

    @GetMapping("/{propertyId}/invalid")
    public ResponseEntity<PropertyResponseDTO> inValidProperty(@PathVariable Long propertyId) {
        return ResponseEntity.ok(propertyService.InvalidProperty(propertyId));
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

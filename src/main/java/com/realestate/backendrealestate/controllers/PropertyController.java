package com.realestate.backendrealestate.controllers;

import com.realestate.backendrealestate.dtos.requests.PropertyRequestDTO;
import com.realestate.backendrealestate.dtos.responses.PropertyResponseDTO;
import com.realestate.backendrealestate.dtos.responses.ResponseDto;
import com.realestate.backendrealestate.entities.PjService;
import com.realestate.backendrealestate.services.PropertyService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

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
    public ResponseEntity<List<PropertyResponseDTO>> getAll() {
        log.info("Getting Properties");
        return ResponseEntity.ok(propertyService.getAll());
    }

    @GetMapping("/{propertyId}")
    public ResponseEntity<PropertyResponseDTO> get(@PathVariable Long propertyId) {
        log.info("Getting property with id: {}", propertyId);
        return ResponseEntity.ok(propertyService.get(propertyId));
    }

    @DeleteMapping("/{propertyId}")
    public void delete(@PathVariable Long propertyId) {
        log.info("Deleting Property with id: {}", propertyId);
        propertyService.delete(propertyId);
    }
}

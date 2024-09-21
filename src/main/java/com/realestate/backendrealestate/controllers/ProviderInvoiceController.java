package com.realestate.backendrealestate.controllers;

import com.realestate.backendrealestate.core.enums.ProviderServiceStatus;
import com.realestate.backendrealestate.core.enums.ReservationStatus;
import com.realestate.backendrealestate.core.enums.ServiceType;
import com.realestate.backendrealestate.dtos.responses.ProviderInvoiceResponseDTO;
import com.realestate.backendrealestate.dtos.responses.ReservationResponseDTO;
import com.realestate.backendrealestate.entities.ProviderInvoice;
import com.realestate.backendrealestate.services.ProviderInvoiceService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/provideInvoices")
@AllArgsConstructor
public class ProviderInvoiceController {

    private final ProviderInvoiceService providerInvoiceService;

    @GetMapping("/reservations/client")
    public ResponseEntity<List<ProviderInvoiceResponseDTO>> getClientReservationsServices(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam(required = false) ProviderServiceStatus status
    ) {
        return ResponseEntity.ok(providerInvoiceService.getClientReservationsServices(status,startDate,endDate));
    }

    @GetMapping("/properties/client")
    public ResponseEntity<List<ProviderInvoiceResponseDTO>> getClientPropertiesServices(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam(required = false) ProviderServiceStatus status) {
        return ResponseEntity.ok(providerInvoiceService.getClientPropertiesServices(status,startDate,endDate));
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<ProviderInvoiceResponseDTO>> getAll(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam(required = false) ProviderServiceStatus status) {
        return ResponseEntity.ok(providerInvoiceService.getAll(status,startDate,endDate));
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<ProviderInvoiceResponseDTO>> getReservationsServices(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam(required = false) ProviderServiceStatus status
    ) {
        return ResponseEntity.ok(providerInvoiceService.getReservationsServices(status,startDate,endDate));
    }

    @GetMapping("/properties")
    public ResponseEntity<List<ProviderInvoiceResponseDTO>> getPropertiesServices(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam(required = false) ProviderServiceStatus status) {
        return ResponseEntity.ok(providerInvoiceService.getPropertiesServices(status,startDate,endDate));
    }

}

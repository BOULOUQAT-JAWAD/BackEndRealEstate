package com.realestate.backendrealestate.controllers;

import com.realestate.backendrealestate.core.enums.ReservationStatus;
import com.realestate.backendrealestate.dtos.requests.ReservationRequestDTO;
import com.realestate.backendrealestate.dtos.responses.ReservationResponseDTO;
import com.realestate.backendrealestate.services.ReservationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/reservations")
@AllArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping("/property")
    public ResponseEntity<List<ReservationResponseDTO>> getPropertyReservations(
            @RequestParam Long propertyId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate checkinDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate checkoutDate,
            @RequestParam(required = false) ReservationStatus status) {

        List<ReservationResponseDTO> reservations = reservationService.getPropertyReservations(propertyId, checkinDate, checkoutDate, status);

        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/client")
    public ResponseEntity<List<ReservationResponseDTO>> getClientReservations(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate checkinDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate checkoutDate,
            @RequestParam(required = false) ReservationStatus status) {

        List<ReservationResponseDTO> reservations = reservationService.getClientReservations(checkinDate, checkoutDate, status);

        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/client/income")
    public ResponseEntity<List<ReservationResponseDTO>> getClientReservationsDateRange(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate checkinDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate checkoutDate,
            @RequestParam(required = false) ReservationStatus status) {

        List<ReservationResponseDTO> reservations = reservationService.getClientReservationsDateRange(checkinDate, checkoutDate, status);

        return ResponseEntity.ok(reservations);
    }

    @PostMapping
    public ResponseEntity<ReservationResponseDTO> save(@RequestBody ReservationRequestDTO reservationRequestDTO){
        log.info("Save Or Update Reservation");
        return ResponseEntity.ok(reservationService.save(reservationRequestDTO));
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<ReservationResponseDTO>> getAll(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate checkinDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate checkoutDate,
            @RequestParam(required = false) ReservationStatus status) {

        List<ReservationResponseDTO> reservations = reservationService.getAll(checkinDate, checkoutDate, status);

        return ResponseEntity.ok(reservations);
    }
}

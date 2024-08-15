package com.realestate.backendrealestate.controllers;

import com.realestate.backendrealestate.core.enums.ReservationStatus;
import com.realestate.backendrealestate.dtos.requests.SubsClientRequest;
import com.realestate.backendrealestate.dtos.responses.ReservationResponseDTO;
import com.realestate.backendrealestate.services.SubscriptionClientService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/subscription")
@AllArgsConstructor
public class SubscriptionClientController {

    private final SubscriptionClientService subscriptionClientService;

    @GetMapping("")
    public ResponseEntity<List<ReservationResponseDTO>> subscribe(@RequestBody SubsClientRequest subsClientRequest) {
        subscriptionClientService.subscribeClient(subsClientRequest);
    }
}

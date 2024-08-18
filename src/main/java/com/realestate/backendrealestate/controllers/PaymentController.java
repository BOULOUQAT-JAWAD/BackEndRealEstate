package com.realestate.backendrealestate.controllers;


import com.realestate.backendrealestate.dtos.requests.PaymentCardRequest;
import com.realestate.backendrealestate.dtos.requests.PaymentChargeRequest;
import com.realestate.backendrealestate.dtos.responses.DefaultResponseDto;
import com.realestate.backendrealestate.dtos.responses.PaymentCardTokenResponse;
import com.realestate.backendrealestate.services.StripeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/payment")
@AllArgsConstructor
public class PaymentController {

    private StripeService stripeService;

    @PostMapping("/card/token")
    @ResponseBody
    public ResponseEntity<PaymentCardTokenResponse> createCardToken(@RequestBody PaymentCardRequest model) {
        return ResponseEntity.ok(stripeService.createCardToken(model));
    }

    @PostMapping("/charge")
    @ResponseBody
    public ResponseEntity<DefaultResponseDto> charge(@RequestBody PaymentChargeRequest model) {
        return ResponseEntity.ok(stripeService.charge(model));
    }


}

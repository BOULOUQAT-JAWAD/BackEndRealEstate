package com.realestate.backendrealestate.controllers;


import com.realestate.backendrealestate.dtos.requests.PjServicesPaymentRequest;
import com.realestate.backendrealestate.dtos.requests.ReservationPaymentRequest;
import com.realestate.backendrealestate.dtos.responses.CheckoutResponse;
import com.realestate.backendrealestate.services.StripeService;
import com.stripe.exception.StripeException;
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


    @PostMapping("/checkout/pjServices")
    public ResponseEntity<CheckoutResponse> checkoutPjServices(@RequestBody PjServicesPaymentRequest pjServicesPaymentRequest)  {
        return ResponseEntity.ok(stripeService.pjServicesCheckout(pjServicesPaymentRequest));
    }

    @PostMapping("/checkout/subscription")
    public ResponseEntity<CheckoutResponse> subscription()  {
        return ResponseEntity.ok(stripeService.subscription());
    }



    @PostMapping("/checkout/reservation")
    public  ResponseEntity<CheckoutResponse> hostedCheckout(@RequestBody ReservationPaymentRequest reservationPaymentRequest) throws StripeException {
        return ResponseEntity.ok(stripeService.reservationCheckout(reservationPaymentRequest));
    }

}

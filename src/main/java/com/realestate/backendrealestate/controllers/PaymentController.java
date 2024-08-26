package com.realestate.backendrealestate.controllers;


import com.realestate.backendrealestate.dtos.requests.PaymentCardRequest;
import com.realestate.backendrealestate.dtos.requests.PaymentChargeRequest;
import com.realestate.backendrealestate.dtos.requests.PjServicesPaymentRequest;
import com.realestate.backendrealestate.dtos.requests.ReservationPaymentRequest;
import com.realestate.backendrealestate.dtos.responses.CheckoutResponse;
import com.realestate.backendrealestate.dtos.responses.DefaultResponseDto;
import com.realestate.backendrealestate.dtos.responses.PaymentCardTokenResponse;
import com.realestate.backendrealestate.services.StripeService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.CustomerCollection;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.CustomerListParams;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

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

    @PostMapping("/checkout")
    @ResponseBody
    public ResponseEntity<DefaultResponseDto> checkout(@RequestBody PaymentChargeRequest model) {
        return ResponseEntity.ok(stripeService.charge(model));
    }

    @PostMapping("/checkout/pjServices")
    public ResponseEntity<CheckoutResponse> checkoutPjServices(@RequestBody PjServicesPaymentRequest pjServicesPaymentRequest)  {
        return ResponseEntity.ok(stripeService.pjServicesCheckout(pjServicesPaymentRequest));
    }

    @PostMapping("/checkout/subscription")
    public ResponseEntity<CheckoutResponse> subscription()  {
        return ResponseEntity.ok(stripeService.subscription());
    }


//
//    @PostMapping("/checkout/reservation")
//    public CheckoutResponse hostedCheckout(@RequestBody ReservationPaymentRequest reservationPaymentRequest) throws StripeException {
//        return ResponseEntity.ok(stripeService.reservationcheckout(reservationPaymentRequest));
//    }

}

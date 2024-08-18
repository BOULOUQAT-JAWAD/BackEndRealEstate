package com.realestate.backendrealestate.services;


import com.realestate.backendrealestate.core.exception.BadRequestException;
import com.realestate.backendrealestate.dtos.requests.PaymentCardRequest;
import com.realestate.backendrealestate.dtos.requests.PaymentChargeRequest;
import com.realestate.backendrealestate.dtos.responses.DefaultResponseDto;
import com.realestate.backendrealestate.dtos.responses.PaymentCardTokenResponse;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Token;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class StripeService {

    @Value("${stripe.apiKey}")
    private String stipeApiKey ;

    public StripeService() {
        Stripe.apiKey = this.stipeApiKey;
    }

    public PaymentCardTokenResponse createCardToken(PaymentCardRequest paymentCardRequest) {
        Stripe.apiKey = this.stipeApiKey;


        try {
            log.info("Creating Card Token {}", paymentCardRequest);
            Map<String, Object> card = new HashMap<>();
            card.put("number", paymentCardRequest.getCardNumber());
            card.put("exp_month", Integer.parseInt(paymentCardRequest.getExpMonth()));
            card.put("exp_year", Integer.parseInt(paymentCardRequest.getExpYear()));
            card.put("cvc", paymentCardRequest.getCvc());
            Map<String, Object> params = new HashMap<>();
            params.put("card", card);
            Token token = Token.create(params);
            if (token == null ) {
                throw new BadRequestException("The given Card is not valid.");
            }
            log.info("Card Token created {}",token);
            return PaymentCardTokenResponse.builder()
                    .cardToken(token.getId())
                    .build();
        } catch (StripeException e) {
            log.error("StripeService (createCardToken)", e);
            throw new RuntimeException(e.getMessage());
        }
    }

    public DefaultResponseDto charge(PaymentChargeRequest chargeRequest) {

        try {
            chargeRequest.setSuccess(false);
            Map<String, Object> chargeParams = new HashMap<>();
            chargeParams.put("amount", chargeRequest.getAmount());
            chargeParams.put("currency", "EUR");
            chargeParams.put("description",
                    "Payment for id " + chargeRequest.getAdditionalInfo().getOrDefault("ID_TAG", ""));
            chargeParams.put("source", chargeRequest.getStripeToken());
            Map<String, Object> metaData = new HashMap<>();
            metaData.put("id", chargeRequest.getChargeId());
            metaData.putAll(chargeRequest.getAdditionalInfo());
            chargeParams.put("metadata", metaData);
            Charge charge = Charge.create(chargeParams);
            chargeRequest.setMessage(charge.getOutcome().getSellerMessage());

            if (charge.getPaid()) {
                chargeRequest.setChargeId(charge.getId());
                chargeRequest.setSuccess(true);

            }
            return DefaultResponseDto.builder()
                    .message("Payment charged successfully")
                    .time(new Date())
                    .status(HttpStatus.OK.getReasonPhrase())
                    .build();
        } catch (StripeException e) {
            log.error("StripeService (charge)", e);
            throw new RuntimeException(e.getMessage());
        }

    }

}

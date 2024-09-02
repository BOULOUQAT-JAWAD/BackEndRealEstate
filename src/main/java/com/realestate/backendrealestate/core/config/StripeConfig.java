package com.realestate.backendrealestate.core.config;


import com.stripe.Stripe;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StripeConfig {

@Value("${stripe.apiKey}")
private String stipeApiKey ;

public void initStripeKey(){
    Stripe.apiKey = stipeApiKey;
}

}

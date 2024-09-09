package com.realestate.backendrealestate.services;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.realestate.backendrealestate.core.enums.ProviderServiceStatus;
import com.realestate.backendrealestate.core.enums.ProviderServiceType;
import com.realestate.backendrealestate.core.enums.ServiceType;
import com.realestate.backendrealestate.core.exception.BadRequestException;
import com.realestate.backendrealestate.dtos.helper.PropertyServiceCheckout;
import com.realestate.backendrealestate.dtos.requests.PaymentCardRequest;
import com.realestate.backendrealestate.dtos.requests.PaymentChargeRequest;
import com.realestate.backendrealestate.dtos.requests.PjServicesPaymentRequest;
import com.realestate.backendrealestate.dtos.responses.CheckoutResponse;
import com.realestate.backendrealestate.dtos.responses.DefaultResponseDto;
import com.realestate.backendrealestate.dtos.responses.PaymentCardTokenResponse;
import com.realestate.backendrealestate.entities.ProviderInvoice;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.model.CustomerCollection;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Subscription;
import com.stripe.model.Token;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.CustomerListParams;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j

public class StripeService {



    @Value("${clientUrl.successPayment}")
    private String clientSuccessPaymentUrl ;

    @Value("${stripe.apiKey}")
    private String stipeApiKey ;

    @Value("${clientUrl.canceledPayment}")
    private String clientCanceledPaymentUrl;

    private final SecurityService securityService;
    private final PropertyService propertyService;
    private final ProviderInvoiceService providerInvoiceService;
    private final PjServicesService pjServicesService;
    private final SubscriptionClientService subscriptionClientService;
    private ObjectMapper objectMapper;

    public StripeService(SecurityService securityService, PropertyService propertyService, ProviderInvoiceService providerInvoiceService, PjServicesService pjServicesService, SubscriptionClientService subscriptionClientService) {
        this.providerInvoiceService = providerInvoiceService;
        this.subscriptionClientService = subscriptionClientService;
        Stripe.apiKey = stipeApiKey;
        this.securityService = securityService;
        this.propertyService = propertyService;
        this.pjServicesService = pjServicesService;
        objectMapper = new ObjectMapper();
    }


    public Customer findOrCreateCustomer(String email)  {
        try {
        CustomerListParams params = CustomerListParams.builder()
                .setEmail(email)
                .setLimit(1L)
                .build();

        CustomerCollection customers = Customer.list(params);

        if (!customers.getData().isEmpty()) {
            return customers.getData().get(0);
        }

        CustomerCreateParams createParams = CustomerCreateParams.builder()
                .setEmail(email)
                .setDescription("New Customer")
                .build();


            return Customer.create(createParams);
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
    }

    public CheckoutResponse pjServicesCheckout(PjServicesPaymentRequest pjServicesPaymentRequest) {
        try {

        Stripe.apiKey = this.stipeApiKey;
        log.info("Finding an existing customer record from Stripe or creating a new one if needed");
//        Customer customer = findOrCreateCustomer("ok@gmail.com");
      Customer customer = findOrCreateCustomer(securityService.getAuthenticatedUser().getEmail());

        BigDecimal totalAmount = pjServicesPaymentRequest.getPropertyServiceCheckouts().stream()
                .map(propertyService -> propertyService.getPjServicesIds().stream()
                        .map(pjServiceId -> BigDecimal.valueOf(pjServicesService.getPjServicePrice(pjServiceId)*100))
                        .reduce(BigDecimal.ZERO, BigDecimal::add))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        log.info("Total of amount all services {} : {}",pjServicesPaymentRequest.getPropertyServiceCheckouts(),totalAmount);

        List<SessionCreateParams.LineItem> lineItemList = pjServicesPaymentRequest.getPropertyServiceCheckouts().stream()
                    .flatMap(propertyServicePayment -> propertyServicePayment.getPjServicesIds().stream()
                            .map(pjServiceId -> SessionCreateParams.LineItem.builder()
                                    .setQuantity(1L)
                                    .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                            .setCurrency("EUR")
                                            .setUnitAmount((long) pjServicesService.getPjServicePrice(pjServiceId))
                                            .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                    .setName(pjServicesService.getPjServiceById(pjServiceId).getTitle())
                                                    .setDescription("Used for property with id :"+propertyServicePayment.getPropertyId())
                                                    .build())
                                            .build())
                                    .build()))
                    .collect(Collectors.toList());




            Map<String, String> metadata = new HashMap<>();
            metadata.put("propertyServiceCheckouts", objectMapper.writeValueAsString(pjServicesPaymentRequest.getPropertyServiceCheckouts()));

            SessionCreateParams paramsBuilder = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setCustomer(customer.getId())
                    .setSuccessUrl("http://localhost:4200/client/servicePayed")
                    .setCancelUrl(clientCanceledPaymentUrl)
                    .addAllLineItem(lineItemList)
                    .putAllMetadata(metadata)
                    .setPaymentIntentData(
                            SessionCreateParams.PaymentIntentData.builder()
                                    .putAllMetadata(metadata)
                                    .build()
                    )
                    .build();

            Session session = Session.create(paramsBuilder);

            return CheckoutResponse.builder()
                    .checkoutUrl(session.getUrl())
                    .build();

        } catch (StripeException | JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    public ResponseEntity<String> handleWebhook(String payload, HttpServletRequest request) {
        String secret = "whsec_74ac8f473398316a69243804d25c3dfd0d224b9f980c4ed12705867a32017577";

        try {
            Event event = Webhook.constructEvent(payload, request.getHeader("Stripe-Signature"), secret);

            switch (event.getType()) {
                case "charge.succeeded":
                    log.info("charge.succeeded for a normal payment");
                    handlePjServicePaymentEvent(event);
                    break;

                case "customer.subscription.created":
                    log.info("customer.subscription.created");
                    handleSubscriptionCreatedEvent(event);
                    break;

                case "payment_intent.payment_failed":
                    log.info("payment failed");
                    // Handle failed payment
                    break;

                // Add more cases for other event types as needed
                default:
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unhandled event type: " + event.getType());
            }

            return ResponseEntity.ok("Webhook handled: " + event.getType());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Webhook error: " + e.getMessage());
        }
    }


    private void handleSubscriptionCreatedEvent(Event event) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(event.getData().getObject().toJson());

        String subscriptionId = rootNode.path("id").asText();
        JsonNode metadataNode = rootNode.path("metadata");

         String clientEmail = metadataNode.path("client").asText();
         String subscriptionType = metadataNode.path("subscription").asText();

         log.info("Received subscription event. Subscription ID: {}, Client: {}, Subscription type: {}",
                    subscriptionId, clientEmail, subscriptionType);

        subscriptionClientService.subscribeClient(clientEmail,subscriptionId);

    }


    private  void handlePjServicePaymentEvent(Event event) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(event.getData().getObject().toJson());

        //getting payment Id
        String paymentId = rootNode.path("id").asText();
        if (!isPaymentAlreadyHandled(paymentId)){
            JsonNode metadataNode = rootNode.path("metadata");
            if (metadataNode.has("propertyServiceCheckouts")){
                String propertyServiceCheckoutsString = metadataNode.path("propertyServiceCheckouts").asText();
                log.info(propertyServiceCheckoutsString);
                try {
                    List<PropertyServiceCheckout> propertyServiceList = objectMapper.readValue(propertyServiceCheckoutsString, new TypeReference<List<PropertyServiceCheckout>>() {});

                    for (PropertyServiceCheckout propertyServiceCheckout : propertyServiceList) {
                        for (Long pjServiceId : propertyServiceCheckout.getPjServicesIds()) {
                            providerInvoiceService
                                    .saveProviderInvoice(ProviderInvoice
                                            .builder()
                                            .pjService(pjServicesService.getPjServiceById(pjServiceId))
                                            .serviceType(ServiceType.property)
                                            .property(propertyService.findPropertyById(propertyServiceCheckout.getPropertyId()))
                                            .date(LocalDate.now())
                                            .status(ProviderServiceStatus.OUVERT)
                                            .gain(0)
                                            .stripePaymentId(paymentId)
                                            .build());
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean isPaymentAlreadyHandled(String paymentId) {
        return !providerInvoiceService.getProviderInvoicesByPaymentId(paymentId).isEmpty();
    }

    public CheckoutResponse subscription() {
        try {

            Stripe.apiKey = this.stipeApiKey;
//            String authenticatedUserEmail = "john.doe5@example.com";

        String authenticatedUserEmail = securityService.getAuthenticatedUser().getEmail();
            log.info("Finding an existing customer record from Stripe or creating a new one if needed");
     //       Customer customer = findOrCreateCustomer("ok@gmail.com");
            Customer customer = findOrCreateCustomer(authenticatedUserEmail);

            BigDecimal totalAmount = BigDecimal.valueOf(pjServicesService.getAnnualClientSubscriptionPrice());

            Map<String, String> metadata = new HashMap<>();
            metadata.put("subscription", "client");
            metadata.put("client", authenticatedUserEmail);

            SessionCreateParams paramsBuilder = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
                    .setCustomer(customer.getId())
                    .setSuccessUrl(clientSuccessPaymentUrl)
                    .setCancelUrl(clientCanceledPaymentUrl)
                    .addLineItem(SessionCreateParams.LineItem.builder()
                            .setQuantity(1L)
                            .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                    .setCurrency("EUR")
                                    .setUnitAmount(totalAmount.longValue())
                                    .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                            .setName("Client subscription fo 1 year")
                                            .setDescription("Client subscription fo 1 year for 100 €.")
                                            .build())
                                    .setRecurring(SessionCreateParams.LineItem.PriceData.Recurring.builder().setInterval(SessionCreateParams.LineItem.PriceData.Recurring.Interval.YEAR).build())

                                    .build())
                            .build())
                    .setSubscriptionData(SessionCreateParams.SubscriptionData
                            .builder()
                            .putAllMetadata(metadata)
                            .build())

                    .build();

            Session session = Session.create(paramsBuilder);

            return CheckoutResponse.builder()
                    .checkoutUrl(session.getUrl())
                    .build();

        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
    }
}

package com.realestate.backendrealestate.services;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.realestate.backendrealestate.core.enums.ProviderServiceStatus;
import com.realestate.backendrealestate.core.enums.ServiceType;
import com.realestate.backendrealestate.dtos.helper.PropertyServiceCheckout;
import com.realestate.backendrealestate.dtos.requests.PjServicesPaymentRequest;
import com.realestate.backendrealestate.dtos.requests.ReservationPaymentRequest;
import com.realestate.backendrealestate.dtos.responses.CheckoutResponse;
import com.realestate.backendrealestate.entities.ProviderInvoice;
import com.realestate.backendrealestate.entities.User;
import com.realestate.backendrealestate.services.smptHandler.MailService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.CustomerCollection;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.CustomerListParams;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
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
    private final ReservationService reservationService;
    private final MailService mailService;
    private ObjectMapper objectMapper;

    public StripeService(SecurityService securityService, PropertyService propertyService, ProviderInvoiceService providerInvoiceService, PjServicesService pjServicesService, SubscriptionClientService subscriptionClientService, ReservationService reservationService, MailService mailService) {
        this.providerInvoiceService = providerInvoiceService;
        this.subscriptionClientService = subscriptionClientService;
        this.reservationService = reservationService;
        this.mailService = mailService;
        Stripe.apiKey = stipeApiKey;
        this.securityService = securityService;
        this.propertyService = propertyService;
        this.pjServicesService = pjServicesService;
        objectMapper = new ObjectMapper();
    }


    public Customer findOrCreateCustomer(User user)  {
        try {
        CustomerListParams params = CustomerListParams.builder()
                .setEmail(user.getEmail())
                .setLimit(1L)
                .build();

        CustomerCollection customers = Customer.list(params);

        if (!customers.getData().isEmpty()) {
            return customers.getData().get(0);
        }

        CustomerCreateParams createParams = CustomerCreateParams.builder()
                .setEmail(user.getEmail())
                .setDescription(user.getRole().toString())
                .build();


            return Customer.create(createParams);
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
    }


    public CheckoutResponse subscription() {
        try {

            Stripe.apiKey = this.stipeApiKey;
//            String authenticatedUserEmail = "john.doe5@example.com";

        User authenticatedUser = securityService.getAuthenticatedUser();
            log.info("Finding an existing customer record from Stripe or creating a new one if needed");
     //       Customer customer = findOrCreateCustomer("ok@gmail.com");
            Customer customer = findOrCreateCustomer(authenticatedUser);

            BigDecimal totalAmount = BigDecimal.valueOf(pjServicesService.getAnnualClientSubscriptionPrice());

            Map<String, String> metadata = new HashMap<>();
            metadata.put("subscription", "client");
            metadata.put("client", authenticatedUser.getEmail());

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

    public CheckoutResponse reservationCheckout(ReservationPaymentRequest reservationPaymentRequest) {
        try {

            Stripe.apiKey = this.stipeApiKey;
            log.info("Finding an existing customer record from Stripe or creating a new one if needed");
//        Customer customer = findOrCreateCustomer("ok@gmail.com");
            Customer customer = findOrCreateCustomer(securityService.getAuthenticatedUser());

            double reservationPrice = reservationService.getReservationById(reservationPaymentRequest.getReservationId()).getPrice();

            double pjServicesPrice = reservationPaymentRequest.getPjServiceIds().stream().mapToDouble(pjServicesService::getPjServicePrice).sum();

            double total = reservationPrice + pjServicesPrice;

            log.info("Total of amount all reservation + services {} : {}",reservationPaymentRequest.getPjServiceIds(),total);

            List<SessionCreateParams.LineItem> lineItemList = new ArrayList<>();

            lineItemList.add(SessionCreateParams.LineItem.builder()
                    .setQuantity(1L)
                    .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                            .setCurrency("EUR")
                            .setUnitAmount((long) reservationPrice)
                            .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                    .setName("reservation")
                                    .setDescription("Payment for reservation having Id "+reservationPaymentRequest.getReservationId())
                                    .build())
                            .build())
                    .build());

            lineItemList.addAll(  reservationPaymentRequest.getPjServiceIds().stream().map(
                    pjServiceId -> SessionCreateParams.LineItem.builder()
                            .setQuantity(1L)
                            .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                    .setCurrency("EUR")
                                    .setUnitAmount((long) pjServicesService.getPjServicePrice(pjServiceId))
                                    .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                            .setName(pjServicesService.getPjServiceById(pjServiceId).getTitle())
                                            .setDescription("Used for reservation with id :"+reservationPaymentRequest.getReservationId())
                                            .build())
                                    .build())
                            .build()
            ).toList());



            Map<String, String> metadata = new HashMap<>();
            metadata.put("reservationId", String.valueOf(reservationPaymentRequest.getReservationId()));
            metadata.put("pjServiceIds", String.valueOf(reservationPaymentRequest.getPjServiceIds()));

            SessionCreateParams paramsBuilder = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setCustomer(customer.getId())
                    .setSuccessUrl("http://localhost:4200?reservation=done")
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

        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
    }



    public CheckoutResponse pjServicesCheckout(PjServicesPaymentRequest pjServicesPaymentRequest) {
        try {

            Stripe.apiKey = this.stipeApiKey;
            log.info("Finding an existing customer record from Stripe or creating a new one if needed");
//        Customer customer = findOrCreateCustomer("ok@gmail.com");
            Customer customer = findOrCreateCustomer(securityService.getAuthenticatedUser());

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
            System.out.println(payload);
            switch (event.getType()) {
                case "charge.succeeded":
                    log.info("charge.succeeded for a normal payment");
                    if (extractReceiptUrl(payload) != null){
                        sendReceiptToCustomer(extractReceiptUrl(payload),extractCustomerEmail(payload));
                    }
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

    private void sendReceiptToCustomer(String receiptLink,String customerEmail) {
            String receipt = mailService.fetchReceiptHtml(receiptLink);
            mailService.send(customerEmail,"Payment succeeded",receipt);
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
            System.out.println("babe   "+metadataNode);
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
            } else if (metadataNode.has("reservationId")) {
                Long reservationId = metadataNode.path("reservationId").asLong();
                String pjServicesIds = metadataNode.path("pjServiceIds").asText();
                log.info("reservation ========== {} - {}",reservationId,pjServicesIds);
                handleReservationPayment(reservationId,pjServicesIds,paymentId);
            }
        }
    }

    private void handleReservationPayment(Long reservationId, String pjServicesIdsStr, String paymentId) {
        //affect reservation tabel (status)
        reservationService.reservationPaid(reservationId);
        //provider invoice
        List<Long> pjServiceIds = convertStringToLongList(pjServicesIdsStr);
        if (!pjServiceIds.isEmpty()){
            for (Long pjServiceId : pjServiceIds){
                providerInvoiceService.saveProviderInvoice(ProviderInvoice.builder()
                                .pjService(pjServicesService.getPjServiceById(pjServiceId))
                                .stripePaymentId(paymentId)
                                .reservation(reservationService.getReservationById(reservationId))
                                .date(LocalDate.now())
                                .serviceType(ServiceType.reservation)
                                .status(ProviderServiceStatus.OUVERT)
                                .build());
            }
        }
        //reservationInvoice
    }

    private boolean isPaymentAlreadyHandled(String paymentId) {
        return !providerInvoiceService.getProviderInvoicesByPaymentId(paymentId).isEmpty();
    }

    public static List<Long> convertStringToLongList(String input) {
        // Check if input is empty or just "[]"
        if (input == null || input.trim().equals("[]")) {
            return new ArrayList<>(); // Return an empty list
        }

        // Remove the square brackets
        input = input.replaceAll("[\\[\\]]", "");

        // Split the string by commas
        String[] stringNumbers = input.split(",");

        // Create a list to hold Long values
        List<Long> longList = new ArrayList<>();

        // Convert each string to Long and add it to the list
        for (String number : stringNumbers) {
            longList.add(Long.parseLong(number.trim()));
        }

        return longList;
    }

    public  String extractReceiptUrl(String jsonString) {
        // Parse the JSON string into a JSONObject
        JSONObject jsonObject = new JSONObject(jsonString);

        // Check if the "data" object exists and extract the necessary fields
        if (jsonObject.has("data")) {
            JSONObject dataObject = jsonObject.getJSONObject("data");
            if (dataObject.has("object")) {
                JSONObject chargeObject = dataObject.getJSONObject("object");
                if (chargeObject.has("receipt_url")) {
                    // Extract and return the "receipt_url" if it exists
                    return chargeObject.getString("receipt_url");
                }
            }
        }

        return null;
    }

    public  String extractCustomerEmail(String jsonString) {
        // Parse the JSON string into a JSONObject
        JSONObject jsonObject = new JSONObject(jsonString);

        String customerEmail = null;

        // Check if the "data" object exists and extract the necessary fields
        if (jsonObject.has("data")) {
            JSONObject dataObject = jsonObject.getJSONObject("data");
            if (dataObject.has("object")) {
                JSONObject chargeObject = dataObject.getJSONObject("object");

                // Extract receipt_email if it exists
                if (chargeObject.has("receipt_email")) {
                    customerEmail = chargeObject.getString("receipt_email");
                }
            }
        }

        return customerEmail; // Return null if no email found
    }


}

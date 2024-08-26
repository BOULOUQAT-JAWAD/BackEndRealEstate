package com.realestate.backendrealestate.services;


import com.realestate.backendrealestate.entities.ProviderInvoice;
import com.realestate.backendrealestate.repositories.ProviderInvoiceRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
@Validated
public class ProviderInvoiceService {

    private final ProviderInvoiceRepository providerInvoiceRepository;

    public ProviderInvoice saveProviderInvoice(ProviderInvoice providerInvoice){
        return providerInvoiceRepository.save(providerInvoice);
    }

    public List<ProviderInvoice> getProviderInvoicesByPaymentId(String paymentId){
        return providerInvoiceRepository.findAllByStripePaymentId(paymentId);
    }


}

package com.realestate.backendrealestate.repositories;

import com.realestate.backendrealestate.entities.ProviderInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProviderInvoiceRepository extends JpaRepository<ProviderInvoice,Long> {
    List<ProviderInvoice> findAllByStripePaymentId(String paymentId);
}

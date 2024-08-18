package com.realestate.backendrealestate.repositories;

import com.realestate.backendrealestate.core.enums.ServiceType;
import com.realestate.backendrealestate.entities.Property;
import com.realestate.backendrealestate.entities.ProviderInvoice;
import com.realestate.backendrealestate.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProviderInvoiceRepository extends JpaRepository<ProviderInvoice,Long> {

    List<ProviderInvoice> findByServiceTypeAndReservation(ServiceType serviceType, Reservation reservation);

    List<ProviderInvoice> findByServiceTypeAndProperty(ServiceType serviceType, Property property);
}

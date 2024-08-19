package com.realestate.backendrealestate.repositories;

import com.realestate.backendrealestate.core.enums.ServiceType;
import com.realestate.backendrealestate.entities.Property;
import com.realestate.backendrealestate.entities.ProviderInvoice;
import com.realestate.backendrealestate.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ProviderInvoiceRepository extends JpaRepository<ProviderInvoice,Long> {

    List<ProviderInvoice> findByServiceTypeAndReservationAndDateBetween(
            ServiceType serviceType,
            Reservation reservation,
            LocalDate startDate,
            LocalDate endDate);

    List<ProviderInvoice> findByServiceTypeAndPropertyAndDateBetween(
            ServiceType serviceType,
            Property property,
            LocalDate startDate,
            LocalDate endDate);
}

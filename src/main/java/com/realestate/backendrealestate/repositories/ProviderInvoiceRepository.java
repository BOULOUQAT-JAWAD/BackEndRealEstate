package com.realestate.backendrealestate.repositories;

import com.realestate.backendrealestate.core.enums.ProviderServiceStatus;
import com.realestate.backendrealestate.core.enums.ServiceType;
import com.realestate.backendrealestate.entities.Property;
import com.realestate.backendrealestate.entities.ProviderInvoice;
import com.realestate.backendrealestate.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ProviderInvoiceRepository extends JpaRepository<ProviderInvoice,Long> {

    @Query("SELECT pi FROM ProviderInvoice pi WHERE " +
            "pi.serviceType = :serviceType AND " +
            "(COALESCE(:status, NULL) IS NULL OR pi.status = :status) AND " +
            "(COALESCE(:startDate, NULL) IS NULL OR pi.date >= :startDate) AND " +
            "(COALESCE(:endDate, NULL) IS NULL OR pi.date <= :endDate)")
    List<ProviderInvoice> findFilteredInvoices(@Param("serviceType") ServiceType serviceType,
                                               @Param("status") ProviderServiceStatus status,
                                               @Param("startDate") LocalDate startDate,
                                               @Param("endDate") LocalDate endDate);
}

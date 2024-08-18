package com.realestate.backendrealestate.services;

import com.realestate.backendrealestate.core.enums.ServiceType;
import com.realestate.backendrealestate.dtos.responses.ProviderInvoiceResponseDTO;
import com.realestate.backendrealestate.dtos.responses.ReservationResponseDTO;
import com.realestate.backendrealestate.entities.Property;
import com.realestate.backendrealestate.entities.ProviderInvoice;
import com.realestate.backendrealestate.mappers.ProviderInvoiceMapper;
import com.realestate.backendrealestate.mappers.ReservationMapper;
import com.realestate.backendrealestate.repositories.ProviderInvoiceRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
@Validated
public class ProviderInvoiceService {

    private final ProviderInvoiceRepository providerInvoiceRepository;
    private final ReservationService reservationService;
    private final ReservationMapper reservationMapper;
    private final PropertyService propertyService;
    private final ProviderInvoiceMapper providerInvoiceMapper;

    public List<ProviderInvoiceResponseDTO> getClientReservationsServices() {
        List<ReservationResponseDTO> reservationResponseDTOS = reservationService.getClientReservations();
        List<ProviderInvoice> providerInvoices = new java.util.ArrayList<>();

        reservationResponseDTOS.forEach(reservationResponseDTO -> {
            List<ProviderInvoice> invoices = providerInvoiceRepository.findByServiceTypeAndReservation(
                    ServiceType.reservation,
                    reservationMapper.toEntity(reservationResponseDTO)
            );
            invoices.forEach(invoice -> providerInvoices.add(invoice.deepCopy())); // Add deep copy
        });

        return providerInvoices.stream()
                .map(providerInvoiceMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<ProviderInvoiceResponseDTO> getClientPropertiesServices() {
        List<Property> properties = propertyService.findPropertiesByClient();
        List<ProviderInvoice> providerInvoices = new java.util.ArrayList<>();

        properties.forEach(property -> {
            List<ProviderInvoice> invoices = providerInvoiceRepository.findByServiceTypeAndProperty(
                    ServiceType.property,
                    property
            );
            invoices.forEach(invoice -> providerInvoices.add(invoice.deepCopy())); // Add deep copy
        });

        return providerInvoices.stream()
                .map(providerInvoiceMapper::toDto)
                .collect(Collectors.toList());
    }
}
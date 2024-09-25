package com.realestate.backendrealestate.services;

import com.realestate.backendrealestate.core.exception.NotFoundException;
import com.realestate.backendrealestate.dtos.responses.PropertyResponseDTO;
import com.realestate.backendrealestate.dtos.responses.ReservationResponseDTO;
import com.realestate.backendrealestate.entities.ProviderInvoice;
import com.realestate.backendrealestate.core.enums.ProviderServiceStatus;
import com.realestate.backendrealestate.core.enums.ServiceType;
import com.realestate.backendrealestate.dtos.responses.ProviderInvoiceResponseDTO;
import com.realestate.backendrealestate.entities.Reservation;
import com.realestate.backendrealestate.entities.ProviderInvoice;
import com.realestate.backendrealestate.entities.Reservation;
import com.realestate.backendrealestate.mappers.ProviderInvoiceMapper;
import com.realestate.backendrealestate.repositories.ProviderInvoiceRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
@Validated
public class ProviderInvoiceService {

    private final ProviderInvoiceRepository providerInvoiceRepository;
    private final ProviderInvoiceMapper providerInvoiceMapper;
    private final PropertyService propertyService;
    private final ReservationService reservationService;


    public ProviderInvoice saveProviderInvoice(ProviderInvoice providerInvoice){
        return providerInvoiceRepository.save(providerInvoice);
    }

    public ProviderInvoiceResponseDTO updateStatus(long providerInvoiceId, ProviderServiceStatus status){

        ProviderInvoice providerInvoice = providerInvoiceRepository.findById(
                providerInvoiceId
        ).orElseThrow(
                () -> new NotFoundException("Invoice not found")
        );
        providerInvoice.setStatus(status);
        ProviderInvoice newInvoice = providerInvoiceRepository.save(providerInvoice);
        return providerInvoiceMapper.toDto(newInvoice);
    }

    public List<ProviderInvoice> getProviderInvoicesByPaymentId(String paymentId){
        return providerInvoiceRepository.findAllByStripePaymentId(paymentId);
    }

    public List<ProviderInvoiceResponseDTO> getFilteredInvoices(ServiceType serviceType, ProviderServiceStatus status, LocalDate startDate, LocalDate endDate) {
        List<ProviderInvoice> providerInvoices = providerInvoiceRepository.findFilteredInvoices(
                serviceType,
                status,
                startDate,
                endDate
        );

        return providerInvoices.stream()
                .map(providerInvoiceMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<ProviderInvoice> getProviderInvoicesByProviderId(Long providerId){
        return providerInvoiceRepository.findAllByProviderProviderId(providerId);
    }

    public List<ProviderInvoice> getProviderInvoicesByReservationId(Long reservationId) {
        return providerInvoiceRepository.findAllByReservationReservationId(reservationId);
    }



    public List<ProviderInvoiceResponseDTO> getClientReservationsServices(ProviderServiceStatus status, LocalDate startDate, LocalDate endDate) {
        List<Long> reservationIds = reservationService.getClientReservations().stream()
                .map(ReservationResponseDTO::getReservationId)
                .toList();

        return getProviderInvoicesForIds(reservationIds, status, startDate, endDate, ServiceType.reservation);
    }

    public List<ProviderInvoiceResponseDTO> getClientPropertiesServices(ProviderServiceStatus status, LocalDate startDate, LocalDate endDate) {
        List<Long> propertyIds = propertyService.getClientProperties(true, true).stream()
                .map(PropertyResponseDTO::getPropertyId)
                .toList();

        return getProviderInvoicesForIds(propertyIds, status, startDate, endDate, ServiceType.property);
    }

    public List<ProviderInvoiceResponseDTO> getAll(ProviderServiceStatus status, LocalDate startDate, LocalDate endDate) {
        List<Long> propertyIds = propertyService.getAll().stream()
                .map(PropertyResponseDTO::getPropertyId)
                .toList();

        return getProviderInvoicesForIds(propertyIds, status, startDate, endDate, ServiceType.property);
    }

    public List<ProviderInvoiceResponseDTO> getReservationsServices(ProviderServiceStatus status, LocalDate startDate, LocalDate endDate) {
        List<Long> reservationIds = reservationService.getAll().stream()
                .map(Reservation::getReservationId)
                .toList();

        return getProviderInvoicesForIds(reservationIds, status, startDate, endDate, ServiceType.reservation);
    }

    public List<ProviderInvoiceResponseDTO> getPropertiesServices(ProviderServiceStatus status, LocalDate startDate, LocalDate endDate) {
        List<Long> propertyIds = propertyService.getAll().stream()
                .map(PropertyResponseDTO::getPropertyId)
                .toList();

        return getProviderInvoicesForIds(propertyIds, status, startDate, endDate, ServiceType.property);
    }

    private List<ProviderInvoiceResponseDTO> getProviderInvoicesForIds(List<Long> ids, ProviderServiceStatus status, LocalDate startDate, LocalDate endDate, ServiceType serviceType) {
        List<ProviderInvoiceResponseDTO> providerInvoiceDTOs = new ArrayList<>();

        for (Long id : ids) {
            List<ProviderInvoice> providerInvoices = serviceType == ServiceType.reservation
                    ? providerInvoiceRepository.getReservationServices(id, status, startDate, endDate)
                    : providerInvoiceRepository.getPropertyServices(id, status, startDate, endDate);

            List<ProviderInvoiceResponseDTO> invoices = providerInvoices.stream()
                    .map(providerInvoiceMapper::toDto)
                    .toList();

            providerInvoiceDTOs.addAll(invoices);
        }

        return providerInvoiceDTOs;
    }

}

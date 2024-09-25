package com.realestate.backendrealestate.services;


import com.realestate.backendrealestate.core.exception.NotFoundException;
import com.realestate.backendrealestate.dtos.responses.DefaultResponseDto;
import com.realestate.backendrealestate.dtos.responses.PjServiceResponseDTO;
import com.realestate.backendrealestate.dtos.responses.ReservationResponseDTO;
import com.realestate.backendrealestate.dtos.responses.ReservationServiceResponseDto;
import com.realestate.backendrealestate.dtos.responses.TravelerResponseDto;
import com.realestate.backendrealestate.entities.ProviderInvoice;
import com.realestate.backendrealestate.entities.Reservation;
import com.realestate.backendrealestate.entities.Traveler;
import com.realestate.backendrealestate.entities.User;
import com.realestate.backendrealestate.repositories.TravelerRepository;
import com.realestate.backendrealestate.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
@Validated
public class TravelerService {

    private final TravelerRepository travelerRepository;
    private final SecurityService securityService;
    private final UserRepository userRepository;
    private final ProviderInvoiceService providerInvoiceService;

    public TravelerService(TravelerRepository travelerRepository, SecurityService securityService, UserRepository userRepository,@Lazy ProviderInvoiceService providerInvoiceService) {
        this.travelerRepository = travelerRepository;
        this.securityService = securityService;
        this.userRepository = userRepository;
        this.providerInvoiceService = providerInvoiceService;
    }

    public void saveTraveler(Traveler traveler){
        travelerRepository.save(traveler);
    }

    public Traveler getAuthenticatedTraveler(){
        try {
            return travelerRepository.findByUser(
                    securityService.getAuthenticatedUser()
            ).orElseThrow(
                    () -> new NotFoundException("Traveler not found")
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public List<TravelerResponseDto> getAllTravelers() {
        List<Traveler> travelerList = travelerRepository.findAll();
        List<TravelerResponseDto> travelerResponseDtos = new ArrayList<>();

        for (Traveler traveler : travelerList){
            travelerResponseDtos.add(TravelerResponseDto.builder()
                            .travelerId(traveler.getTravelerId())
                            .email(traveler.getUser().getEmail())
                            .activated(traveler.getUser().getActivated())
                            .firstName(traveler.getUser().getFirstName())
                            .lastName(traveler.getUser().getLastName())
                            .phoneNumber(traveler.getUser().getPhoneNumber())
                            .reservations(getReservationResponseDtos(traveler.getReservations()))
                    .build());
        }

        return travelerResponseDtos;
    }

    private List<ReservationResponseDTO> getReservationResponseDtos(List<Reservation> reservations) {
        List<ReservationResponseDTO> reservationResponseDTOS = new ArrayList<>();
        for (Reservation reservation : reservations){
            reservationResponseDTOS.add(ReservationResponseDTO.builder()
                            .reservationId(reservation.getReservationId())
                            .price(reservation.getPrice())
                            .checkinDate(reservation.getCheckinDate())
                            .checkoutDate(reservation.getCheckoutDate())
                            .status(reservation.getStatus())
                            .propertyId(reservation.getProperty().getPropertyId())
                            .reservationServices(getReservationServices(reservation))
                    .build());
        }
        return reservationResponseDTOS;
    }

    private List<ReservationServiceResponseDto> getReservationServices(Reservation reservation) {
        List<ProviderInvoice> providerInvoiceList = providerInvoiceService.getProviderInvoicesByReservationId(reservation.getReservationId());
        List<ReservationServiceResponseDto> reservationServiceResponseDtos = new ArrayList<>();
        for (ProviderInvoice providerInvoice : providerInvoiceList){
            reservationServiceResponseDtos.add(
                    ReservationServiceResponseDto.builder()
                            .providerId(providerInvoice.getProvider() != null ? providerInvoice.getProvider().getProviderId() : null)
                            .reservationId(providerInvoice.getReservation().getReservationId())
                            .pjService(
                                    PjServiceResponseDTO.builder()
                                            .pjServiceId(providerInvoice.getPjService().getPjServiceId())
                                            .pjServiceType(providerInvoice.getPjService().getPjServiceType())
                                            .title(providerInvoice.getPjService().getTitle())
                                            .description(providerInvoice.getPjService().getDescription())
                                            .price(providerInvoice.getPjService().getPrice())
                                            .build()
                            )
                            .date(providerInvoice.getDate())
                            .gain(providerInvoice.getGain())
                            .rating(providerInvoice.getRating())
                            .status(providerInvoice.getStatus())
                            .stripePaymentId(providerInvoice.getStripePaymentId())
                            .InvoiceId(providerInvoice.getProviderInvoiceId())
                            .build()
            );
        }
        return reservationServiceResponseDtos;
    }

    public DefaultResponseDto changeTravelerActivation(Long travelerId) {
        User travelerUser = travelerRepository.findById(travelerId).get().getUser();
        if (travelerUser.getActivated()){
            travelerUser.setActivated(false);
        }
        else {
            travelerUser.setActivated(true);
        }
        userRepository.save(travelerUser);
        return DefaultResponseDto.builder()
                .message("traveler activation changed")
                .time(new Date())
                .status(HttpStatus.OK.getReasonPhrase())
                .build();
    }
}

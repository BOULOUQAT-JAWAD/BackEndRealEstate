package com.realestate.backendrealestate.mappers;

import com.realestate.backendrealestate.dtos.responses.ProviderInvoiceResponseDTO;
import com.realestate.backendrealestate.entities.ProviderInvoice;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProviderInvoiceMapper {

    @Mapping(source = "provider.providerId", target = "providerId")
    @Mapping(source = "property.propertyId", target = "propertyId")
    @Mapping(source = "reservation.reservationId", target = "reservationId")
    ProviderInvoiceResponseDTO toDto(ProviderInvoice providerInvoice);

}

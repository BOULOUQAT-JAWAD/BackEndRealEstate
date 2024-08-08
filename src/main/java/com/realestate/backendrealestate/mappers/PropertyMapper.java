package com.realestate.backendrealestate.mappers;

import com.realestate.backendrealestate.dtos.requests.PropertyRequestDTO;
import com.realestate.backendrealestate.dtos.responses.PropertyResponseDTO;
import com.realestate.backendrealestate.entities.Property;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PropertyMapper {

    Property toEntity(PropertyRequestDTO propertyRequestDTO);
    PropertyResponseDTO toDto(Property property);
    @Mapping(target = "id", ignore = true) // Ignore ID during update
    void updatePropertyFromDto(PropertyRequestDTO dto, @MappingTarget Property entity);

}

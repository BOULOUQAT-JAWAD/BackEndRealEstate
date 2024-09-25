package com.realestate.backendrealestate.mappers;

import com.realestate.backendrealestate.dtos.requests.PjServiceRequestDTO;
import com.realestate.backendrealestate.dtos.responses.PjServiceResponseDTO;
import com.realestate.backendrealestate.entities.PjService;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PjServiceMapper {

    PjService toEntity(PjServiceRequestDTO pjServiceRequestDTO);
    PjServiceResponseDTO toDto(PjService pjService);

//    @Mapping(target = "id", ignore = true) // Ignore ID during update
//    void updatePropertyFromDto(PropertyRequestDTO dto, @MappingTarget Property entity);

}

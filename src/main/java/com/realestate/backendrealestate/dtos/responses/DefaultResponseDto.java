package com.realestate.backendrealestate.dtos.responses;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class DefaultResponseDto {
    private String status;
    private String message;
    private Date time;
}
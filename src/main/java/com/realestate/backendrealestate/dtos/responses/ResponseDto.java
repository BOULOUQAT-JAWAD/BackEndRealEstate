package com.realestate.backendrealestate.dtos.responses;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDto {
    protected String status;
    protected String message;
    protected Date time;
}

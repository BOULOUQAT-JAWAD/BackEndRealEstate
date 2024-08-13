package com.realestate.backendrealestate.controllers.exceptionHandler;


import com.realestate.backendrealestate.core.exception.InternalServerException;
import com.realestate.backendrealestate.core.exception.RealEstateGlobalException;
import com.realestate.backendrealestate.dtos.responses.DefaultResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@ControllerAdvice
@Slf4j
public class RealEstateExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = {RealEstateGlobalException.class})
    public ResponseEntity<DefaultResponseDto> handleException(RealEstateGlobalException exception) {
        HttpStatus responseStatus;
        if (exception instanceof InternalServerException){
            responseStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        } else {
            responseStatus = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<>(DefaultResponseDto.builder()
                .message(exception.getMessage())
                .status(responseStatus.getReasonPhrase())
                .time(new Date())
                .build(),
                responseStatus);
    }

}
package com.realestate.backendrealestate.core.exception;

public class BadRequestException extends RealEstateGlobalException{
    public BadRequestException(String message) {
        super(message);
    }
}

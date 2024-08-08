package com.realestate.backendrealestate.services;

import com.realestate.backendrealestate.entities.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@AllArgsConstructor
@Slf4j
@Validated
public class AuthService {

    public static User getAuthenticatedUser(){
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}

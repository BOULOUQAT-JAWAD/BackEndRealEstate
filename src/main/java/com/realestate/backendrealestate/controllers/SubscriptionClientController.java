package com.realestate.backendrealestate.controllers;


import com.realestate.backendrealestate.dtos.responses.SubscriptionClientResponse;
import com.realestate.backendrealestate.services.SubscriptionClientService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/client/subscription")
@AllArgsConstructor
public class SubscriptionClientController {

    private final SubscriptionClientService subscriptionClientService;


    @GetMapping("/{userId}")
    public SubscriptionClientResponse getClientSubscription(@PathVariable Long userId){
        return subscriptionClientService.getClientSubscription(userId);
    }
}

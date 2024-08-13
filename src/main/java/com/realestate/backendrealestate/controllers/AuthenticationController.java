package com.realestate.backendrealestate.controllers;


import com.realestate.backendrealestate.dtos.requests.AuthRequestDto;
import com.realestate.backendrealestate.dtos.requests.SignUpRequestDto;
import com.realestate.backendrealestate.dtos.responses.AuthResponseDto;
import com.realestate.backendrealestate.dtos.responses.DefaultResponseDto;
import com.realestate.backendrealestate.services.SecurityService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final SecurityService securityService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody AuthRequestDto authRequestDto) {
        log.info("AuthRequestDto has been received.");
        return new ResponseEntity<>(securityService.authenticateUser(authRequestDto), HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<DefaultResponseDto> signUp(@Valid @RequestBody SignUpRequestDto signUpRequestDto) {
        log.info("SignUpRequestDto has been received.");
        return new ResponseEntity<>(securityService.registerUser(signUpRequestDto), HttpStatus.OK);
    }

    @GetMapping("/activate/{tokenVerification}")
    public ResponseEntity<AuthResponseDto> activateUser(@PathVariable String tokenVerification) {
        log.info("Verification token has been received.");
        return new ResponseEntity<>(securityService.activateUser(tokenVerification), HttpStatus.OK);
    }


}

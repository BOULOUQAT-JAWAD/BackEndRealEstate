package com.realestate.backendrealestate.services;


import com.realestate.backendrealestate.core.enums.TokenType;
import com.realestate.backendrealestate.core.exception.RealEstateGlobalException;
import com.realestate.backendrealestate.dtos.request.AuthRequestDto;
import com.realestate.backendrealestate.dtos.request.SignUpRequestDto;
import com.realestate.backendrealestate.dtos.response.AuthResponseDto;
import com.realestate.backendrealestate.dtos.response.DefaultResponseDto;
import com.realestate.backendrealestate.entities.User;
import com.realestate.backendrealestate.entities.UserToken;
import com.realestate.backendrealestate.security.jwt.JwtUtils;
import com.realestate.backendrealestate.services.smptHandler.MailContentBuilder;
import com.realestate.backendrealestate.services.smptHandler.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;


@Service
@Slf4j
@RequiredArgsConstructor
public class SecurityService {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final MailContentBuilder mailContentBuilder;
    private final UserDetailsService userDetailsService;

    @Value("${verification.url}")
    private String verificationBaseUrl;

    public AuthResponseDto authenticateUser(AuthRequestDto authRequestDto) {
        try {
            log.info("Authentication request has been validated.");
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authRequestDto.getEmail(),
                    authRequestDto.getPassword()));

            org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
            log.info("User with email {} is authenticated ", user.getUsername());
            User userInfo = userService.getUserByEmail(user.getUsername());
            return AuthResponseDto.builder()
                    .email(user.getUsername())
                    .firstName(userInfo.getFirstName())
                    .lastName(userInfo.getLastName())
                    .phoneNumber(userInfo.getPhoneNumber())
                    .userId(userInfo.getUserId())
                    .token(jwtUtils.generateToken(user))
                    .build();
        } catch (AuthenticationException e) {
            log.error("Couldn't authenticate the user : ",e);
            throw new RealEstateGlobalException(e.getMessage());

        }
    }

    @Transactional
    public DefaultResponseDto registerUser(SignUpRequestDto signUpRequestDto) {
        log.info("User sign-up request has been validated.");
        if (userService.checkUserByMail(signUpRequestDto.getEmail())) {
            log.error("Another user with this email {} already exist!",signUpRequestDto.getEmail());
            throw new RealEstateGlobalException("Another user with this email " + signUpRequestDto.getEmail() + " already exist!");
        }
        log.info("Creating a new user");
        User newUser = User
                .builder()
                .firstName(signUpRequestDto.getFirstName())
                .lastName(signUpRequestDto.getLastName())
                .phoneNumber(signUpRequestDto.getPhoneNumber())
                .email(signUpRequestDto.getEmail())
                .password(passwordEncoder.encode(signUpRequestDto.getPassword()))
                .role(signUpRequestDto.getRole())
                .userToken(UserToken.builder().token(generateUserToken()).type(TokenType.ACTIVATION).build())
                .activated(false)
                .build();
        userService.saveUser(newUser);
        log.info("Sending the user a verification link to his email!");
        mailService.send(newUser.getEmail(),"Verification Email", mailContentBuilder.build(verificationBaseUrl + newUser.getUserToken().getToken()));

        return DefaultResponseDto.builder()
                .message("user saved successfully!")
                .time(new Date())
                .status(HttpStatus.OK.getReasonPhrase())
                .build();
    }
    public String generateUserToken(){
        return UUID.randomUUID().toString();
    }


    @Transactional
    public AuthResponseDto activateUser(String tokenVerification) {
        UserToken userToken = UserToken.builder()
                .token(tokenVerification)
                .type(TokenType.ACTIVATION)
                .build();
        User userToActivate = userService.findByToken(userToken);
        if (userToActivate.getActivated()) {
            log.error("User is already activated");
            throw new RealEstateGlobalException("User is already activated");
        }
        userToActivate.setActivated(true);
        userToActivate.setUserToken(null);
        userService.saveUser(userToActivate);
        userService.deleteUserToken(userToken);
        log.info("User is activated successfully");
        return authenticateUser(userToActivate);
    }

    private AuthResponseDto authenticateUser(User userToActivate) {
        try {
            log.info("Authenticating user using userCredentials object! ");
            UserDetails userDetails = userDetailsService.loadUserByUsername(userToActivate.getEmail());
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(userDetails,"",userDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);
            org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
            log.info("User with email {} is authenticated ", user.getUsername());
            User userInfo = userService.getUserByEmail(user.getUsername());
            return AuthResponseDto.builder()
                    .email(user.getUsername())
                    .firstName(userInfo.getFirstName())
                    .lastName(userInfo.getLastName())
                    .phoneNumber(userInfo.getPhoneNumber())
                    .userId(userInfo.getUserId())
                    .token(jwtUtils.generateToken(user))
                    .build();
        } catch (AuthenticationException e) {
            log.error("Couldn't authenticate the user : ",e);
            throw new RealEstateGlobalException(e.getMessage());

        }
    }


    public User getAuthenticatedUser(){
        org.springframework.security.core.userdetails.User
                user = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userService.getUserByEmail(user.getUsername());
    }

}

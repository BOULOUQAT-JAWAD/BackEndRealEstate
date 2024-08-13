package com.realestate.backendrealestate.services;


import com.realestate.backendrealestate.core.exception.RealEstateGlobalException;
import com.realestate.backendrealestate.entities.User;
import com.realestate.backendrealestate.entities.UserToken;
import com.realestate.backendrealestate.repositories.UserRepository;
import com.realestate.backendrealestate.repositories.UserTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserTokenRepository userTokenRepository;


    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> {log.error("user not found");
            return new RealEstateGlobalException("user not found");});
    }

    public boolean checkUserByMail(String email) {
        Optional<User> userCredentialEntityOptional = userRepository.findByEmail(email);
        return userCredentialEntityOptional.isPresent();
    }

    public User saveUser(User newUser) {
        return userRepository.save(newUser);
    }

    public User findByToken(UserToken userToken) {
        return userRepository.findByUserTokenTokenAndUserTokenType(userToken.getToken(),userToken.getType())
                .orElseThrow(() -> {log.error("user not found");
            return new RealEstateGlobalException("user not found");});
    }

    public void deleteUserToken(UserToken userToken) {
        userTokenRepository.deleteByToken(userToken.getToken());
    }
}

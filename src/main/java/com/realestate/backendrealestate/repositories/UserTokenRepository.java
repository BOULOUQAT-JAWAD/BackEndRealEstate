package com.realestate.backendrealestate.repositories;

import com.realestate.backendrealestate.entities.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTokenRepository extends JpaRepository<UserToken, Long> {
    void deleteByToken(String token);
}

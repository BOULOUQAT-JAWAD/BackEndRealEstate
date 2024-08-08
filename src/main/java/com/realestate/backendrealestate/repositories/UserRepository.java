package com.realestate.backendrealestate.repositories;

import com.realestate.backendrealestate.entities.User;
import com.realestate.backendrealestate.entities.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUserToken(UserToken userToken);
}

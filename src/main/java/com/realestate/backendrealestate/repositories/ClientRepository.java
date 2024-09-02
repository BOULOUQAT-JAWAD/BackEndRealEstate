package com.realestate.backendrealestate.repositories;

import com.realestate.backendrealestate.entities.Client;
import com.realestate.backendrealestate.entities.User;
import org.hibernate.validator.internal.util.logging.Log_$logger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository  extends JpaRepository<Client, Long> {
    Optional<Client> findByUser(User user);
    Optional<Client> findByUserEmail(String userEmail);
    Optional<Client> findByUserUserId(Long userId);

}

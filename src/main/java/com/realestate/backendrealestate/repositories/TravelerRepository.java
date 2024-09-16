package com.realestate.backendrealestate.repositories;

import com.realestate.backendrealestate.entities.Client;
import com.realestate.backendrealestate.entities.Traveler;
import com.realestate.backendrealestate.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TravelerRepository extends JpaRepository<Traveler,Long> {
    Optional<Traveler> findByUser(User user);

}

package com.realestate.backendrealestate.repositories;

import com.realestate.backendrealestate.entities.Client;
import com.realestate.backendrealestate.entities.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {

    List<Property> findByClient(Client client);
}

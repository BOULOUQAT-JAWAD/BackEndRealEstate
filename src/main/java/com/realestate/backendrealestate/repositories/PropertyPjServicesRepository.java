package com.realestate.backendrealestate.repositories;

import com.realestate.backendrealestate.entities.Property;
import com.realestate.backendrealestate.entities.PropertyPjServices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyPjServicesRepository extends JpaRepository<PropertyPjServices, Long> {
    List<PropertyPjServices> findAllByProperty(Property property);
}

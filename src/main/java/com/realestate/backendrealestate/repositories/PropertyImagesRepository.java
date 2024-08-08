package com.realestate.backendrealestate.repositories;

import com.realestate.backendrealestate.entities.Property;
import com.realestate.backendrealestate.entities.PropertyImages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PropertyImagesRepository extends JpaRepository<PropertyImages,Long> {
    List<PropertyImages> findAllByProperty(Property property);

}

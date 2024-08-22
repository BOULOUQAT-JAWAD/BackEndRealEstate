package com.realestate.backendrealestate.repositories;

import com.realestate.backendrealestate.entities.Client;
import com.realestate.backendrealestate.entities.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {

    List<Property> findByClient(Client client);
    @Query("SELECT p FROM Property p WHERE " +
            "p.client = :client AND " +
            "(COALESCE(:publish, NULL) IS NULL OR p.publish = :publish) AND " +
            "(COALESCE(:valid, NULL) IS NULL OR p.valid = :valid)")
    List<Property> findFilteredProperties(@Param("client") Client client,
                                          @Param("publish") Boolean publish,
                                          @Param("valid") Boolean valid);
    List<Property> findByClientAndPublishAndValid(Client client, boolean publish, boolean valid);
}

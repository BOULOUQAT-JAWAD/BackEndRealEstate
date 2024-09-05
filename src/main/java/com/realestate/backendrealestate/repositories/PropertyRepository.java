package com.realestate.backendrealestate.repositories;

import com.realestate.backendrealestate.core.enums.PropertyType;
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
    List<Property> findByClientAndPublishAndValid(Client client, Boolean publish, Boolean valid);
    @Query("SELECT p FROM Property p WHERE " +
            "(COALESCE(:description, NULL) IS NULL OR LOWER(p.description) LIKE :description) AND " +
            "(COALESCE(:country, NULL) IS NULL OR LOWER(p.country) LIKE :country) AND " +
            "(COALESCE(:city, NULL) IS NULL OR LOWER(p.city) LIKE :city) AND " +
            "(:propertyType IS NULL OR p.propertyType = :propertyType) AND " +
            "(:minNumberOfRooms IS NULL OR p.numberOfRooms >= :minNumberOfRooms) AND " +
            "(:maxNumberOfRooms IS NULL OR p.numberOfRooms <= :maxNumberOfRooms) AND " +
            "(:minNumberOfPersons IS NULL OR p.numberOfPersons >= :minNumberOfPersons) AND " +
            "(:maxNumberOfPersons IS NULL OR p.numberOfPersons <= :maxNumberOfPersons) AND " +
            "(:minSurface IS NULL OR p.surface >= :minSurface) AND " +
            "(:maxSurface IS NULL OR p.surface <= :maxSurface) AND " +
            "(:minPricePerNight IS NULL OR p.pricePerNight >= :minPricePerNight) AND " +
            "(:maxPricePerNight IS NULL OR p.pricePerNight <= :maxPricePerNight) AND " +
            "p.publish = true AND " +
            "p.valid = true")
    List<Property> findFilteredProperties(
            @Param("description") String description,
            @Param("country") String country,
            @Param("city") String city,
            @Param("propertyType") PropertyType propertyType,
            @Param("minNumberOfRooms") Integer minNumberOfRooms,
            @Param("maxNumberOfRooms") Integer maxNumberOfRooms,
            @Param("minNumberOfPersons") Integer minNumberOfPersons,
            @Param("maxNumberOfPersons") Integer maxNumberOfPersons,
            @Param("minSurface") Integer minSurface,
            @Param("maxSurface") Integer maxSurface,
            @Param("minPricePerNight") Double minPricePerNight,
            @Param("maxPricePerNight") Double maxPricePerNight
    );
}

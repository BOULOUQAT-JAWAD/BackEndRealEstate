package com.realestate.backendrealestate.repositories;

import com.realestate.backendrealestate.core.enums.ReservationStatus;
import com.realestate.backendrealestate.entities.Property;
import com.realestate.backendrealestate.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation,Long> {

    List<Reservation> findByProperty(Property property);

    /*
    •	COALESCE Function:
	•	The COALESCE(:parameter, NULL) returns NULL if the parameter is NULL, which effectively ignores that condition if the filter is not provided. This allows you to pass any combination of parameters to filter the reservations.
	•	If a filter parameter is provided (e.g., checkinDate), it will be used in the query. If it’s not provided (i.e., null), the corresponding condition is ignored.
    */
    @Query("SELECT r FROM Reservation r WHERE " +
            "(r.property.id = :propertyId) AND " +
            "(COALESCE(:checkinDate, NULL) IS NULL OR r.checkinDate >= :checkinDate) AND " +
            "(COALESCE(:checkoutDate, NULL) IS NULL OR r.checkoutDate <= :checkoutDate) AND " +
            "(COALESCE(:status, NULL) IS NULL OR r.status = :status)")
    List<Reservation> findFilteredReservations(@Param("propertyId") Long propertyId,
                                               @Param("checkinDate") LocalDate checkinDate,
                                               @Param("checkoutDate") LocalDate checkoutDate,
                                               @Param("status") ReservationStatus status);
}

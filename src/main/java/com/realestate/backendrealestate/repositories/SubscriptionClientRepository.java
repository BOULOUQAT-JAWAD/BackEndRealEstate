package com.realestate.backendrealestate.repositories;


import com.realestate.backendrealestate.entities.SubscriptionClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionClientRepository extends JpaRepository<SubscriptionClient, Integer> {
}

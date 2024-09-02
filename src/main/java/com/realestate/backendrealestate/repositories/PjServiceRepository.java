package com.realestate.backendrealestate.repositories;

import com.realestate.backendrealestate.core.enums.PjServiceType;
import com.realestate.backendrealestate.entities.PjService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PjServiceRepository extends JpaRepository<PjService,Long> {
    Optional<PjService> findByPjServiceType(PjServiceType pjServiceType);
}

package com.realestate.backendrealestate.repositories;

import com.realestate.backendrealestate.entities.PjService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PjServiceRepository extends JpaRepository<PjService,Long> {
}

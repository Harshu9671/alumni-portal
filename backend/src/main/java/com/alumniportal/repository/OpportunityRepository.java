package com.alumniportal.repository;

import com.alumniportal.model.Opportunity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OpportunityRepository extends JpaRepository<Opportunity, Long> {
    List<Opportunity> findAllByOrderByPostedAtDesc();
}

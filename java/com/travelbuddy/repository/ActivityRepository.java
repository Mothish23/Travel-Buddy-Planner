package com.travelbuddy.repository;

import com.travelbuddy.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
    List<Activity> findByTripIdOrderByActivityDateAsc(Long tripId);
    Optional<Activity> findByIdAndTripUserEmail(Long id, String email);
}
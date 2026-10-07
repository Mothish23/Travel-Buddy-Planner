package com.travelbuddy.repository;

import com.travelbuddy.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TripRepository extends JpaRepository<Trip, Long> {
    List<Trip> findByUserEmailOrderByStartDateDesc(String email);
    Optional<Trip> findByIdAndUserEmail(Long id, String email);
}
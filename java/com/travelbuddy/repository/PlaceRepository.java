package com.travelbuddy.repository;

import com.travelbuddy.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlaceRepository extends JpaRepository<Place, Long> {
    List<Place> findByTripIdOrderByIdDesc(Long tripId);
    Optional<Place> findByIdAndTripUserEmail(Long id, String email);
}
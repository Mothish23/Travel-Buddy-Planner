package com.travelbuddy.controller;

import com.travelbuddy.entity.Trip;
import com.travelbuddy.repository.TripRepository;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final TripRepository tripRepository;

    public AdminController(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    @GetMapping("/trips")
    public List<Trip> getAllTrips() {
        return tripRepository.findAll();
    }

    @DeleteMapping("/trips/{id}")
    public void deleteTrip(@PathVariable Long id) {
        tripRepository.deleteById(id);
    }
}
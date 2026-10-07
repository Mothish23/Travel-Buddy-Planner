package com.travelbuddy.controller;

import com.travelbuddy.entity.Trip;
import com.travelbuddy.service.TripService;

import jakarta.validation.Valid;

import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trips")
public class TripController {

    private final TripService tripService;

    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    @GetMapping
    public ResponseEntity<List<Trip>> getMyTrips(Authentication authentication) {
        return ResponseEntity.ok(tripService.getMyTrips(authentication.getName()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Trip> getTrip(@PathVariable Long id, Authentication authentication) {
        return ResponseEntity.ok(tripService.getTrip(id, authentication.getName()));
    }

    @PostMapping
    public ResponseEntity<Trip> createTrip(@Valid @RequestBody Trip trip, Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tripService.createTrip(trip, authentication.getName()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Trip> updateTrip(@PathVariable Long id, @Valid @RequestBody Trip trip, Authentication authentication) {
        return ResponseEntity.ok(tripService.updateTrip(id, trip, authentication.getName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrip(@PathVariable Long id, Authentication authentication) {
        tripService.deleteTrip(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}
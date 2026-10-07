package com.travelbuddy.controller;

import com.travelbuddy.entity.Place;
import com.travelbuddy.service.PlaceService;

import jakarta.validation.Valid;

import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PlaceController {

    private final PlaceService placeService;

    public PlaceController(PlaceService placeService) {
        this.placeService = placeService;
    }

    @GetMapping("/trips/{tripId}/places")
    public ResponseEntity<List<Place>> getPlaces(@PathVariable Long tripId, Authentication authentication) {
        return ResponseEntity.ok(placeService.getPlaces(tripId, authentication.getName()));
    }

    @PostMapping("/trips/{tripId}/places")
    public ResponseEntity<Place> createPlace(@PathVariable Long tripId, @Valid @RequestBody Place place, Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED).body(placeService.createPlace(tripId, place, authentication.getName()));
    }

    @PutMapping("/places/{id}")
    public ResponseEntity<Place> updatePlace(@PathVariable Long id, @Valid @RequestBody Place place, Authentication authentication) {
        return ResponseEntity.ok(placeService.updatePlace(id, place, authentication.getName()));
    }

    @DeleteMapping("/places/{id}")
    public ResponseEntity<Void> deletePlace(@PathVariable Long id, Authentication authentication) {
        placeService.deletePlace(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}
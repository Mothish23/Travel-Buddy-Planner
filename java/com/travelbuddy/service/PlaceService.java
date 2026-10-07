package com.travelbuddy.service;

import com.travelbuddy.entity.Place;
import com.travelbuddy.entity.Trip;
import com.travelbuddy.exception.ResourceNotFoundException;
import com.travelbuddy.repository.PlaceRepository;
import com.travelbuddy.repository.TripRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlaceService {

    private final PlaceRepository placeRepository;
    private final TripRepository tripRepository;

    public PlaceService(PlaceRepository placeRepository, TripRepository tripRepository) {
        this.placeRepository = placeRepository;
        this.tripRepository = tripRepository;
    }

    public List<Place> getPlaces(Long tripId, String email) {
        getTrip(tripId, email);
        return placeRepository.findByTripIdOrderByIdDesc(tripId);
    }

    public Place createPlace(Long tripId, Place place, String email) {
        Trip trip = getTrip(tripId, email);
        place.setTrip(trip);
        return placeRepository.save(place);
    }

    public Place updatePlace(Long id, Place updated, String email) {
        Place existing = placeRepository.findByIdAndTripUserEmail(id, email).orElseThrow(() -> new ResourceNotFoundException("Place not found"));
        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        existing.setImageUrl(updated.getImageUrl());
        return placeRepository.save(existing);
    }

    public void deletePlace(Long id, String email) {
        Place place = placeRepository.findByIdAndTripUserEmail(id, email).orElseThrow(() -> new ResourceNotFoundException("Place not found"));
        placeRepository.delete(place);
    }

    private Trip getTrip(Long tripId, String email) {
        return tripRepository.findByIdAndUserEmail(tripId, email).orElseThrow(() -> new ResourceNotFoundException("Trip not found"));
    }
}
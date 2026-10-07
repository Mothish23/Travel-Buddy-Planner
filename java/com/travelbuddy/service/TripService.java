package com.travelbuddy.service;

import com.travelbuddy.entity.Trip;
import com.travelbuddy.entity.User;
import com.travelbuddy.exception.ResourceNotFoundException;
import com.travelbuddy.repository.TripRepository;
import com.travelbuddy.repository.UserRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TripService {

    private final TripRepository tripRepository;

    private final UserRepository userRepository;

    public TripService(TripRepository tripRepository, UserRepository userRepository) {
        this.tripRepository = tripRepository;
        this.userRepository = userRepository;
    }

    public List<Trip> getMyTrips(String email) {
        return tripRepository.findByUserEmailOrderByStartDateDesc(email);
    }

    public Trip getTrip(Long id, String email) {
        return tripRepository.findByIdAndUserEmail(id, email).orElseThrow(() -> new ResourceNotFoundException("Trip not found"));
    }

    public Trip createTrip(Trip trip, String email) {
        validateDates(trip);
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        trip.setUser(user);
        return tripRepository.save(trip);
    }

    public Trip updateTrip(Long id, Trip updatedTrip, String email) {
        Trip existing = getTrip(id, email);
        validateDates(updatedTrip);
        existing.setTripName(updatedTrip.getTripName());
        existing.setDestination(updatedTrip.getDestination());
        existing.setStartDate(updatedTrip.getStartDate());
        existing.setEndDate(updatedTrip.getEndDate());
        existing.setBudget(updatedTrip.getBudget());
        existing.setDescription(updatedTrip.getDescription());
        existing.setImageUrl(updatedTrip.getImageUrl());
        return tripRepository.save(existing);
    }

    public void deleteTrip(Long id, String email) {
        Trip trip = getTrip(id, email);
        tripRepository.delete(trip);
    }

    private void validateDates(Trip trip) {
        if (trip.getStartDate() != null && trip.getEndDate() != null && trip.getEndDate().isBefore(trip.getStartDate())) {
            throw new IllegalArgumentException("End date cannot be before start date");
        }
    }
}
package com.travelbuddy.service;

import com.travelbuddy.entity.Activity;
import com.travelbuddy.entity.Trip;
import com.travelbuddy.exception.ResourceNotFoundException;
import com.travelbuddy.repository.ActivityRepository;
import com.travelbuddy.repository.TripRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final TripRepository tripRepository;

    public ActivityService(ActivityRepository activityRepository, TripRepository tripRepository) {
        this.activityRepository = activityRepository;
        this.tripRepository = tripRepository;
    }

    public List<Activity> getActivities(Long tripId, String email) {
        getTrip(tripId, email);
        return activityRepository.findByTripIdOrderByActivityDateAsc(tripId);
    }

    public Activity createActivity(Long tripId, Activity activity, String email) {
        Trip trip = getTrip(tripId, email);
        activity.setTrip(trip);
        return activityRepository.save(activity);
    }

    public Activity updateActivity(Long id, Activity updated, String email) {
        Activity existing = activityRepository.findByIdAndTripUserEmail(id, email).orElseThrow(() -> new ResourceNotFoundException("Activity not found"));
        existing.setName(updated.getName());
        existing.setActivityDate(updated.getActivityDate());
        existing.setDescription(updated.getDescription());
        existing.setCost(updated.getCost());
        return activityRepository.save(existing);
    }

    public void deleteActivity(Long id, String email) {
        Activity activity = activityRepository.findByIdAndTripUserEmail(id, email).orElseThrow(() -> new ResourceNotFoundException("Activity not found"));
        activityRepository.delete(activity);
    }

    private Trip getTrip(Long tripId, String email) {
        return tripRepository.findByIdAndUserEmail(tripId, email).orElseThrow(() -> new ResourceNotFoundException("Trip not found"));
    }
}
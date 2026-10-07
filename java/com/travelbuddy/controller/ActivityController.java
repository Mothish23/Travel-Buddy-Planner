package com.travelbuddy.controller;

import com.travelbuddy.entity.Activity;
import com.travelbuddy.service.ActivityService;

import jakarta.validation.Valid;

import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ActivityController {
    private final ActivityService activityService;

    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @GetMapping("/trips/{tripId}/activities")
    public ResponseEntity<List<Activity>> getActivities(@PathVariable Long tripId, Authentication authentication) {
        return ResponseEntity.ok(activityService.getActivities(tripId, authentication.getName()));
    }

    @PostMapping("/trips/{tripId}/activities")
    public ResponseEntity<Activity> createActivity(@PathVariable Long tripId, @Valid @RequestBody Activity activity, Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED).body(activityService.createActivity(tripId, activity, authentication.getName()));
    }

    @PutMapping("/activities/{id}")
    public ResponseEntity<Activity> updateActivity(@PathVariable Long id, @Valid @RequestBody Activity activity, Authentication authentication) {
        return ResponseEntity.ok(activityService.updateActivity(id, activity, authentication.getName()));
    }

    @DeleteMapping("/activities/{id}")
    public ResponseEntity<Void> deleteActivity(@PathVariable Long id, Authentication authentication) {
        activityService.deleteActivity(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}
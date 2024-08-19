package com.app.shwe.controller;

import com.app.shwe.model.Activity;
import com.app.shwe.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/activities")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @PostMapping
    public ResponseEntity<String> createActivity(@RequestBody Activity activity) {
        try {
            if (activity == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Request data is null.");
            }
            activityService.saveActivity(activity);
            return ResponseEntity.status(HttpStatus.OK).body("Activity saved successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while saving Activity: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Activity> getActivityById(@PathVariable("id") int id) {
        Optional<Activity> activity = activityService.getActivityById(id);
        return activity.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Activity> updateActivity(@PathVariable("id") int id, @RequestBody Activity updatedActivity) {
        try {
            Activity activity = activityService.updateActivity(id, updatedActivity);
            return new ResponseEntity<>(activity, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActivity(@PathVariable("id") int id) {
        activityService.deleteActivity(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<Page<Activity>> getAllActivities(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Activity> activities = activityService.getAllActivities(page, size);
        return new ResponseEntity<>(activities, HttpStatus.OK);
    }
}

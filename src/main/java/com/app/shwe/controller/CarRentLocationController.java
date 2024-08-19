package com.app.shwe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.app.shwe.model.CarRentLocation;
import com.app.shwe.service.CarRentLocationService;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/carRentLocation")
public class CarRentLocationController {

    @Autowired
    private CarRentLocationService carRentLocationService;

    // Get all CarRentLocations with pagination
    @GetMapping
    public ResponseEntity<?> getAllCarRentLocations(Pageable pageable) {
        try {
            Page<CarRentLocation> carRentLocations = carRentLocationService.getAllCarRentLocations(pageable);
            return new ResponseEntity<>(carRentLocations, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error occurred while fetching CarRentLocations: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // // Get CarRentLocation by ID
    // @GetMapping("/{id}")
    // public ResponseEntity<?> getCarRentLocationById(@PathVariable int id) {
    // try {
    // Optional<CarRentLocation> carRentLocation =
    // carRentLocationService.getCarRentLocationById(id);
    // return carRentLocation.map(value -> new ResponseEntity<>(value,
    // HttpStatus.OK))
    // .orElseGet(() -> new ResponseEntity<>("CarRentLocation not found with ID: " +
    // id,
    // HttpStatus.NOT_FOUND));
    // } catch (Exception e) {
    // return new ResponseEntity<>("Error occurred while fetching CarRentLocation: "
    // + e.getMessage(),
    // HttpStatus.INTERNAL_SERVER_ERROR);
    // }
    // }

    // Create a new CarRentLocation

    @GetMapping("/location")
    public ResponseEntity<?> getAllLocations() {
        return carRentLocationService.getAllLocations();
    }

    @PostMapping
    public ResponseEntity<?> createCarRentLocation(@RequestBody CarRentLocation carRentLocation) {
        try {
            CarRentLocation savedCarRentLocation = carRentLocationService.saveCarRentLocation(carRentLocation);
            return new ResponseEntity<>(savedCarRentLocation, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error occurred while creating CarRentLocation: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Update an existing CarRentLocation
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCarRentLocation(@PathVariable int id, @RequestBody CarRentLocation carRentLocation) {
        try {
            Optional<CarRentLocation> existingCarRentLocation = carRentLocationService.getCarRentLocationById(id);

            if (existingCarRentLocation.isPresent()) {
                carRentLocation.setId(id); // Ensure the ID remains the same
                CarRentLocation updatedCarRentLocation = carRentLocationService.saveCarRentLocation(carRentLocation);
                return new ResponseEntity<>(updatedCarRentLocation, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("CarRentLocation not found with ID: " + id, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error occurred while updating CarRentLocation: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Delete a CarRentLocation by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCarRentLocationById(@PathVariable int id) {
        try {
            Optional<CarRentLocation> existingCarRentLocation = carRentLocationService.getCarRentLocationById(id);

            if (existingCarRentLocation.isPresent()) {
                carRentLocationService.deleteCarRentLocationById(id);
                return new ResponseEntity<>("CarRentLocation deleted successfully", HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>("CarRentLocation not found with ID: " + id, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error occurred while deleting CarRentLocation: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

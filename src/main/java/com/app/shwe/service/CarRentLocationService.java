package com.app.shwe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.app.shwe.dto.LocationResponse;
import com.app.shwe.model.CarRentLocation;
import com.app.shwe.repository.CarRentLocationRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CarRentLocationService {

    @Autowired
    private CarRentLocationRepository carRentLocationRepository;

    public Page<CarRentLocation> getAllCarRentLocations(Pageable pageable) {
        return carRentLocationRepository.findAll(pageable);
    }

    public ResponseEntity<?> getAllLocations() {
        try {
            List<LocationResponse> carPrices = carRentLocationRepository.findAllUniqueLocation();

            return new ResponseEntity<>(carPrices, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public Optional<CarRentLocation> getCarRentLocationById(int id) {
        return carRentLocationRepository.findById(id);
    }

    public CarRentLocation saveCarRentLocation(CarRentLocation carRentLocation) {
        return carRentLocationRepository.save(carRentLocation);
    }

    public void deleteCarRentLocationById(int id) {
        carRentLocationRepository.deleteById(id);
    }
}

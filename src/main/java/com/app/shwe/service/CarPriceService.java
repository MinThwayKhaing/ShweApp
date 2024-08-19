package com.app.shwe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.app.shwe.dto.CarPriceRequest;
import com.app.shwe.dto.PriceRequest;
import com.app.shwe.dto.TypeResponse;
import com.app.shwe.model.CarPrice;
import com.app.shwe.model.CarRentLocation;
import com.app.shwe.repository.CarPriceRepository;
import com.app.shwe.repository.CarRentLocationRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CarPriceService {

    @Autowired
    private CarPriceRepository carPriceRepository;
    @Autowired
    private CarRentLocationRepository carRentLocationRepo;

    public ResponseEntity<?> getAllCarPrices() {
        try {
            List<TypeResponse> carPrices = carPriceRepository.findAllUniqueTypes();

            return new ResponseEntity<>(carPrices, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> getPriceByLocationAndType(PriceRequest req) {
        try {
            System.out.println("Location: " + req.getLocation() + ", Type: " + req.getType());
            Optional<Double> price = carPriceRepository.findPriceByLocationAndType(req.getLocation(), req.getType());
            return new ResponseEntity<>(price, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public ResponseEntity<Optional<CarPrice>> getCarPriceById(int id) {
        try {
            Optional<CarPrice> carPrice = carPriceRepository.findById(id);
            return new ResponseEntity<>(carPrice, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(Optional.empty(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<String> saveCarPrice(CarPriceRequest dto) {
        try {
            CarPrice model = new CarPrice();
            model.setPrice(dto.getPrice());
            model.setType(dto.getType());

            Optional<CarRentLocation> location = carRentLocationRepo.findById(dto.getCarRentLocationId());
            if (location.isPresent()) {
                model.setCarRentLocation(location.get());
            } else {
                return new ResponseEntity<>("CarRentLocation not found", HttpStatus.NOT_FOUND);
            }

            carPriceRepository.save(model);
            return new ResponseEntity<>("Created Successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<CarPrice> updateCarPrice(int id, CarPriceRequest dto) {

        ResponseEntity<Optional<CarPrice>> response = getCarPriceById(id);

        if (response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (response.getBody().isPresent()) {
            CarPrice model = new CarPrice();
            model.setId(id);
            model.setPrice(dto.getPrice());
            model.setType(dto.getType());
            Optional<CarRentLocation> location = carRentLocationRepo.findById(dto.getCarRentLocationId());
            model.setCarRentLocation(location.get());
            CarPrice savedCarPrice = carPriceRepository.save(model);
            return new ResponseEntity<>(savedCarPrice, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    public ResponseEntity<String> deleteCarPriceById(int id) {
        try {
            carPriceRepository.deleteById(id);
            return new ResponseEntity<>("Car Price deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

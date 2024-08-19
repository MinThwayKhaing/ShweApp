package com.app.shwe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.app.shwe.dto.CarPriceRequest;
import com.app.shwe.dto.PriceRequest;
import com.app.shwe.model.CarPrice;
import com.app.shwe.service.CarPriceService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/carprice")
public class CarPriceController {

    @Autowired
    private CarPriceService carPriceService;

    @GetMapping("/types")
    public ResponseEntity<?> getAllCarTypes() {
        return carPriceService.getAllCarPrices();
    }

    @PostMapping("/price")
    public ResponseEntity<?> getAllCarPrices(@RequestBody PriceRequest req) {
        return carPriceService.getPriceByLocationAndType(req);
    }

    // Get CarPrice by ID
    @GetMapping("/{id}")
    public ResponseEntity<CarPrice> getCarPriceById(@PathVariable int id) {
        ResponseEntity<Optional<CarPrice>> response = carPriceService.getCarPriceById(id);
        return response.getBody().map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Create a new CarPrice
    @PostMapping
    public ResponseEntity<?> createCarPrice(@RequestBody CarPriceRequest carPrice) {
        return carPriceService.saveCarPrice(carPrice);
    }

    // Update an existing CarPrice
    @PutMapping("/{id}")
    public ResponseEntity<CarPrice> updateCarPrice(@PathVariable int id, @RequestBody CarPrice carPrice) {
        return updateCarPrice(id, carPrice);
    }

    // Delete a CarPrice by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCarPriceById(@PathVariable int id) {
        ResponseEntity<Optional<CarPrice>> response = carPriceService.getCarPriceById(id);

        if (response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (response.getBody().isPresent()) {
            return carPriceService.deleteCarPriceById(id);
        } else {
            return new ResponseEntity<>("Car Price with ID " + id + " not found", HttpStatus.NOT_FOUND);
        }
    }
}

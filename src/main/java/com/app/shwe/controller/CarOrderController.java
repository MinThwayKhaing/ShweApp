package com.app.shwe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.app.shwe.dto.CarOrderRequestDTO;
import com.app.shwe.service.CarOrderService;

@RestController
@RequestMapping("/api/carOrders")
public class CarOrderController {

    @Autowired
    private CarOrderService carOrderService;

    @PostMapping
    public ResponseEntity<?> createCarOrder(@RequestBody CarOrderRequestDTO dto) {
        return carOrderService.createCarOrder(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCarOrderById(@PathVariable Long id) {
        return carOrderService.getCarOrderById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCarOrder(@PathVariable Long id, @RequestBody CarOrderRequestDTO dto) {
        return carOrderService.updateCarOrder(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCarOrder(@PathVariable Long id) {
        return carOrderService.deleteCarOrder(id);
    }

    // @GetMapping
    // public ResponseEntity<?> getAllCarOrders(@RequestParam(required = false)
    // String searchString, Pageable pageable) {
    // return carOrderService.getAllCarOrders(searchString, pageable);
    // }
}

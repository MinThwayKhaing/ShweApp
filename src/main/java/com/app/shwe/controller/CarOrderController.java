package com.app.shwe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.shwe.dto.CarOrderRequestDTO;
import com.app.shwe.dto.CarOrderResponseDTO;
import com.app.shwe.dto.SearchDTO;
import com.app.shwe.service.CarOrderService;

@RestController
@RequestMapping("/api/v1/carOrders")
public class CarOrderController {

    @Autowired
    private CarOrderService carOrderService;

    @PostMapping("/createCarOrder")
    public ResponseEntity<ResponseEntity<String>> createCarOrder(@RequestBody CarOrderRequestDTO dto) {
        return ResponseEntity.ok(carOrderService.createCarOrder(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCarOrderById(@PathVariable int id) {
        return carOrderService.getCarOrderById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCarOrder(@PathVariable int id, @RequestBody CarOrderRequestDTO dto) {
        return carOrderService.updateCarOrder(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCarOrder(@PathVariable int id) {
        return carOrderService.deleteCarOrder(id);
    }
    
    @GetMapping("/showCarOrder/{id}")
	public ResponseEntity<Page<CarOrderResponseDTO>> showCarOrders(@PathVariable int id,@RequestBody SearchDTO dto) {
		return ResponseEntity.ok(carOrderService.showCarOrders(id, dto));
	}

    @PutMapping("/cancelCarOrder/{id}")
	public ResponseEntity<ResponseEntity<String>> cancelOrder(@PathVariable int id,@RequestBody CarOrderRequestDTO dto){
		return ResponseEntity.ok(carOrderService.cancelCarOrder(id,dto));
	}
}

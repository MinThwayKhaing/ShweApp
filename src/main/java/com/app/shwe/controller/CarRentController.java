package com.app.shwe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.app.shwe.dto.CarRentDTO;
import com.app.shwe.dto.CarRentRequestDTO;
import com.app.shwe.dto.ResponseDTO;
import com.app.shwe.dto.SearchDTO;
import com.app.shwe.model.CarRent;
import com.app.shwe.service.CarRentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/carRent")
@RequiredArgsConstructor
public class CarRentController {

	@Autowired
	private CarRentService carService;

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/carRentSave")
	public ResponseEntity<ResponseDTO> saveCars(@RequestBody CarRentRequestDTO dto) {
		return ResponseEntity.ok(carService.saveCars(dto));
	}

	@GetMapping("/getCarById/{id}")
	public ResponseEntity<CarRentDTO> getCarById(@PathVariable int id) {
		return carService.findCarById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}
	
	@PutMapping("/updateCarRent/{id}")
	public ResponseEntity<ResponseDTO> updateCarRent(@PathVariable int id,@RequestBody CarRentRequestDTO dto){
		return ResponseEntity.ok(carService.updateCarRent(id,dto));
	}

	@DeleteMapping("/deleteCar/{id}")
	public ResponseEntity<ResponseDTO> deleteCar(@PathVariable  int id){
		return ResponseEntity.ok(carService.deleteCar(id));
	}
	
	@GetMapping("/showAllCarsAndSearch")
	public ResponseEntity<Page<CarRent>> showCars(@RequestBody SearchDTO search){
		return ResponseEntity.ok(carService.showAllCar(search));
	}
	

}

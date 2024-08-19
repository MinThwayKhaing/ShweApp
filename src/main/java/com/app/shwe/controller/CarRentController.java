package com.app.shwe.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.app.shwe.dto.CarRentDTO;
import com.app.shwe.dto.CarRentRequestDTO;
import com.app.shwe.dto.ResponseDTO;
import com.app.shwe.dto.SearchDTO;
import com.app.shwe.dto.TranslatorRequestDTO;
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
	public ResponseEntity<ResponseEntity<String>> saveCars(@RequestPart("image") MultipartFile carImage,
			@RequestPart("dto") CarRentRequestDTO dto) {
		return ResponseEntity.ok(carService.saveCars(carImage, dto));
	}

	@GetMapping("/getCarById/{id}")
	public ResponseEntity<CarRentDTO> getCarById(@PathVariable int id) {
		return carService.findCarById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@GetMapping("/types")
	public ResponseEntity<?> getAllCarTypes() {
		return carService.getAllCarTypes();
	}

	@GetMapping("/brands")
	public ResponseEntity<?> findAllBrands() {
		return carService.findAllBrands();
	}

	@PutMapping("/updateCarRent/{id}")
	public ResponseEntity<String> updateCarRent(@PathVariable int id, @RequestPart("image") MultipartFile carImage,
			@RequestPart("request") CarRentRequestDTO dto) {
		return carService.updateCarRent(id, carImage, dto);
	}

	@DeleteMapping("/deleteCar/{id}")
	public ResponseEntity<?> deleteCar(@PathVariable int id) {
		return carService.deleteCar(id);
	}

	@GetMapping("/showAllCarsAndSearch")
	public Page<CarRent> showCars(@RequestBody SearchDTO search) {
		return carService.showAllCar(search);
	}

}

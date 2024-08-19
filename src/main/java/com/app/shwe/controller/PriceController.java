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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import com.app.shwe.dto.PriceRequestDTO;
import com.app.shwe.dto.SearchDTO;
import com.app.shwe.model.Price;
import com.app.shwe.service.PriceService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/price")
@RequiredArgsConstructor
public class PriceController {

	@Autowired
	private PriceService priceService;

	@PostMapping("/savePrice")
	public ResponseEntity<String> savePrice(@RequestBody PriceRequestDTO request) {
		return priceService.savePrice(request);
	}

	@GetMapping("/getPriceById/{id}")
	public ResponseEntity<Price> getPriceById(@PathVariable int id) {
		return priceService.getPriceById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@GetMapping("/getAllPrice")
	public Page<Price> showPrice(@RequestBody SearchDTO search) {
		return priceService.getAllPrice(search);
	}

	@PutMapping("/updatePirce/{id}")
	public ResponseEntity<String> updatePrice(@PathVariable int id, @RequestBody PriceRequestDTO request) {
		return priceService.updatePrice(id, request);
	}

	@DeleteMapping("/deletePriceById/{id}")
	public ResponseEntity<?> deletePrice(@PathVariable int id) {
		return priceService.deletePirceById(id);
	}

}

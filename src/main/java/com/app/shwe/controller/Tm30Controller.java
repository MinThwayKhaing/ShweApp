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
import org.springframework.web.multipart.MultipartFile;

import com.app.shwe.dto.SearchDTO;
import com.app.shwe.dto.Tm30RequestDTO;
import com.app.shwe.model.Tm30;
import com.app.shwe.service.Tm30Service;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/tm30")
@RequiredArgsConstructor
public class Tm30Controller {

	@Autowired
	private Tm30Service tm30Service;

	@PostMapping("/saveTm30")
	public ResponseEntity<String> saveNews(@RequestPart("passport") MultipartFile passportPage,
			@RequestPart("visa") MultipartFile visaPage, @RequestPart("request") Tm30RequestDTO request) {
		return tm30Service.saveTm30(passportPage, visaPage, request);

	}

	@GetMapping("/getTm30ById/{id}")
	public ResponseEntity<Tm30> getTm30ById(@PathVariable int id) {
		return tm30Service.getTm30ById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@GetMapping("/getAllTm30")
	public Page<Tm30> showCars(@RequestBody SearchDTO search) {
		return tm30Service.getAllTm30(search);
	}

	@PutMapping("/updateTm30/{id}")
	public ResponseEntity<String> updateNews(@PathVariable int id, @RequestPart("passport") MultipartFile passportPage,
			@RequestPart("visa") MultipartFile visaPage, @RequestPart("request") Tm30RequestDTO request) {
		return tm30Service.updateTm30(id, passportPage,visaPage, request);
	}
	
	@DeleteMapping("/deleteTm30/{id}")
	public ResponseEntity<?> deleteCar(@PathVariable int id) {
		return tm30Service.deleteTm30(id);
	}
}
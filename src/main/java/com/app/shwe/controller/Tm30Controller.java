package com.app.shwe.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.app.shwe.dto.SearchDTO;
import com.app.shwe.dto.Tm30DTO;
import com.app.shwe.dto.Tm30DTOResponseDTO;
import com.app.shwe.dto.Tm30DetailsDTO;
import com.app.shwe.dto.Tm30RequestDTO;
import com.app.shwe.dto.Tm30ResponseDTO;
import com.app.shwe.dto.VisaResponseDTO;
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
	public ResponseEntity<String> saveTm30(@RequestPart("passport") MultipartFile passportPage,
			@RequestPart("visa") MultipartFile visaPage, @RequestPart("request") Tm30RequestDTO request) {
		return tm30Service.saveTm30(passportPage, visaPage, request);

	}

	@GetMapping("/getTm30ById/{id}")
	public ResponseEntity<Tm30> getTm30ById(@PathVariable int id) {
		return tm30Service.getTm30ById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@GetMapping("/getOrderBySysKey/{sysKey}")
	public ResponseEntity<Tm30ResponseDTO> getOrderBySysKey(@PathVariable String sysKey) {
		Tm30ResponseDTO response = tm30Service.getOrderBySysKey(sysKey);
		if (response == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@GetMapping("/details/{id}")
	public ResponseEntity<?> getTm30DetailsById(@PathVariable int id) {
		return tm30Service.getTm30DetailsById(id);
	}

	@GetMapping("/getAllTm30")
	public Page<Tm30DTOResponseDTO> showTmAllTm30(@RequestParam(required = false) String searchString,
			@RequestParam(required = false) String status,
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int size) {
		return tm30Service.getTm30(searchString, status, page, size);
	}

	// @GetMapping("/getAllTm30Order")
	// public Page<VisaResponseDTO> getAllTm30Order(@RequestBody SearchDTO search) {
	// return tm30Service.getAllTm30(search);
	// }

	// @GetMapping("/getAllTm30Order")
	// public Page<VisaResponseDTO> getAllTm30Order(@RequestBody SearchDTO search) {
	// return tm30Service.getAllTm30(search);
	// }

	// @GetMapping("/getAllTm30")
	// public Page<Tm30ResponseDTO> showTm30s(@RequestBody SearchDTO search) {
	// return tm30Service.getAllTm30(search);
	// }

	// @PutMapping("/updateTm30/{id}")
	// public ResponseEntity<String> updateNews(@PathVariable int id,
	// @RequestPart("passport") MultipartFile passportPage,
	// @RequestPart("visa") MultipartFile visaPage, @RequestPart("request")
	// Tm30RequestDTO request) {
	// return tm30Service.updateTm30(id, passportPage, visaPage, request);
	// }

	@DeleteMapping("/deleteTm30/{id}")
	public ResponseEntity<?> deleteCar(@PathVariable int id) {
		return tm30Service.deleteTm30(id);
	}

	// @GetMapping("/getTm30OrderByUserId")
	// public List<Tm30DTO> getTm30OrderByuserId() {
	// return tm30Service.getTm30OrderByUserId();
	// }

	// @GetMapping("/getAllTm30Order")
	// public List<Tm30ResponseDTO> getAllTm30Order() {
	// return tm30Service.getAllTm30Order();
	// }

	@PutMapping("/cancelOrder/{id}")
	public ResponseEntity<String> cancelOrder(@PathVariable int id) {
		return tm30Service.cancelOrder(id);
	}

	@PutMapping("/onProgress/{id}")
	public ResponseEntity<String> onProgress(@PathVariable int id) {
		return tm30Service.onProgress(id);
	}

	@PutMapping("/completed/{id}")
	public ResponseEntity<String> completed(@PathVariable int id) {
		return tm30Service.completed(id);
	}
}

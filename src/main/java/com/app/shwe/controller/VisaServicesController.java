package com.app.shwe.controller;

import java.util.List;

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

import com.app.shwe.dto.NewsRequestDTO;
import com.app.shwe.dto.SearchDTO;
import com.app.shwe.dto.VisaServiceRequestDTO;
import com.app.shwe.model.News;
import com.app.shwe.model.VisaServices;
import com.app.shwe.service.VisaService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/visa")
@RequiredArgsConstructor
public class VisaServicesController {
	
	@Autowired
	private VisaService visaService;
	
	@PostMapping("/saveVisa")
	public ResponseEntity<String> saveNews(@RequestPart("images") MultipartFile images,
			@RequestPart("request") VisaServiceRequestDTO request) {
		return visaService.saveVisa(images, request);

	}
	
	@GetMapping("/getVisaById/{id}")
	public ResponseEntity<VisaServices> getNewsById(@PathVariable int id) {
		return visaService.getVisaById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}
	
	@GetMapping("/showAllVisa")
	public ResponseEntity<Page<VisaServices>> showAllVisa(@RequestBody SearchDTO search) {
		return ResponseEntity.ok(visaService.getAllVisa(search));
	}
	
	@PutMapping("/updateVisa/{id}")
	public ResponseEntity<String> updateVisa(@PathVariable int id, @RequestPart("image")MultipartFile images,
			@RequestPart("request") VisaServiceRequestDTO request) {
		return visaService.updateVisaService(id, images, request);
	}

	@DeleteMapping("/deleteVisa/{id}")
	public ResponseEntity<?> deleteCar(@PathVariable int id) {
		return visaService.deleteVisa(id);
	}
	
	@GetMapping("/getAllVisa")
	public List<VisaServices> getAllVisaList(){
		return visaService.getAllVisaList();
	}
}

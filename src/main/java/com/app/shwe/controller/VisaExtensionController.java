package com.app.shwe.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.app.shwe.dto.VisaExtensionRequestDTO;
import com.app.shwe.dto.VisaExtensionResponseDTO;
import com.app.shwe.service.VisaExtensionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/visa-extension")
@RequiredArgsConstructor
public class VisaExtensionController {

	@Autowired
	private VisaExtensionService visaService;

	@PostMapping("/saveVisaExtension")
	public ResponseEntity<String> saveTm30(@RequestPart("passportBio") MultipartFile passportBio,
			@RequestPart("visaPage") MultipartFile visaPage, @RequestPart("request") VisaExtensionRequestDTO request) {
		return visaService.saveVisaExtension(passportBio, visaPage, request);

	}

	@GetMapping("/getVisaExtensionByUserId")
	public List<VisaExtensionResponseDTO> getVisaExtensionByUserId() {
		return visaService.getVisaExtensionByOrder();
	}

}

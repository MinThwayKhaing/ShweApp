package com.app.shwe.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.app.shwe.dto.EmbassyLetterRequestDTO;
import com.app.shwe.dto.EmbassyLetterResponseDTO;
import com.app.shwe.dto.Report90DayRequestDTO;
import com.app.shwe.dto.VisaExtensionResponseDTO;
import com.app.shwe.service.EmbassyLetterService;
import com.app.shwe.service.VisaExtensionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/embassy-letter")
@RequiredArgsConstructor
public class EmbassyLetterController {
	
	@Autowired
	private EmbassyLetterService visaService;

	@PostMapping("/saveEmbassyLetter")
	public ResponseEntity<String> saveTm30(@RequestPart("passportBio") MultipartFile passportBio,
			@RequestPart("visaPage") MultipartFile visaPage, @RequestPart("request") EmbassyLetterRequestDTO request) {
		return visaService.saveEmbassyLetter(passportBio, visaPage, request);

	}

	@GetMapping("/getEmbassyLetterByUserId")
	public List<EmbassyLetterResponseDTO> getEmbassyLetterByUserId() {
		return visaService.getEmbassyLetterOrderByUserId();
	}
	
	@PutMapping("/cancelOrder/{id}")
	public ResponseEntity<String> cancelOrder(@PathVariable int id) {
		return visaService.cancelOrder(id);
	}

}

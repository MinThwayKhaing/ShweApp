package com.app.shwe.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.app.shwe.dto.EmbassyLetterDTO;
import com.app.shwe.dto.EmbassyLetterRequestDTO;
import com.app.shwe.dto.EmbassyLetterResponseDTO;
import com.app.shwe.dto.Report90DayRequestDTO;
import com.app.shwe.dto.VisaExtensionDTO;
import com.app.shwe.dto.VisaExtensionResponseDTO;
import com.app.shwe.model.EmbassyVisaType;
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

//	@GetMapping("/getEmbassyLetterByUserId")
//	public List<EmbassyLetterDTO> getEmbassyLetterByUserId() {
//		return visaService.getEmbassyLetterOrderByUserId();
//	}
	
	@GetMapping("/getAllEmbassyVisaOrder")
	public Page<EmbassyLetterDTO> showAllVisaExtensionOrder(@RequestParam(required = false) String searchString,
			@RequestParam(required = false) String status,
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int size) {
		return visaService.getAllEmbassyVisaOrder(searchString, status, page, size);
	}
	
	@PutMapping("/cancelOrder/{id}")
	public ResponseEntity<String> cancelOrder(@PathVariable int id) {
		return visaService.cancelOrder(id);
	}
	
	@PutMapping("/onProgress/{id}")
	public ResponseEntity<String> onProgress(@PathVariable int id) {
		return visaService.onProgress(id);
	}

	@PutMapping("/completed/{id}")
	public ResponseEntity<String> completed(@PathVariable int id) {
		return visaService.completed(id);
	}
	
	 @GetMapping("/getEmbassyLetterOrderById/{sysKey}")
		public ResponseEntity<EmbassyLetterDTO> getVisaTypeById(@PathVariable String sysKey) {
		    return visaService.getEmbassyLetterOrderById(sysKey);
		}

}

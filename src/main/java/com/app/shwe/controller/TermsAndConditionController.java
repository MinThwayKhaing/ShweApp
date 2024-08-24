package com.app.shwe.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.app.shwe.dto.TermsAndConditionDTO;
import com.app.shwe.dto.Tm30RequestDTO;
import com.app.shwe.model.TermsAndCondition;
import com.app.shwe.model.Tm30;
import com.app.shwe.repository.TermsAndConditionRepository;
import com.app.shwe.service.TermsAndConditionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/terms-and-condition")
@RequiredArgsConstructor
public class TermsAndConditionController {
	
	@Autowired
	private TermsAndConditionService termsAndCondtionService;
	
	@PostMapping("/saveTermsAndCondition")
	public ResponseEntity<String> saveTermsAndCondition(@RequestBody TermsAndConditionDTO request) {
		return termsAndCondtionService.saveTermsAndCondition(request);
	}
	
	@GetMapping("/getTermsAndCondition")
	public TermsAndCondition getTermsAndCondition() {
		return termsAndCondtionService.getTermsAndCondition();
	} 

}

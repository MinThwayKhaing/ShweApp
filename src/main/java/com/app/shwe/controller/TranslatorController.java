package com.app.shwe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

import com.app.shwe.dto.ResponseDTO;
import com.app.shwe.dto.SearchDTO;
import com.app.shwe.dto.TranslatorRequestDTO;
import com.app.shwe.model.Translator;
import com.app.shwe.service.TranslatorService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/translator")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class TranslatorController {

	@Autowired
	private TranslatorService translatorService;

	@PostMapping("/translatorSave")
	public ResponseEntity<String> saveTranslator(@RequestPart("image") MultipartFile image,
			@RequestPart("request") TranslatorRequestDTO request) {
		return translatorService.saveTranslator(image, request);
	}

	@GetMapping("/getTransaltorById/{id}")
	public ResponseEntity<Translator> getTranslatorById(@PathVariable int id) {
		return translatorService.getTranslatorById(id).map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PutMapping("/updateTranslator/{id}")
	public ResponseEntity<String> updateTranslator(
			@PathVariable int id,
			@RequestPart("image") MultipartFile image,
			@RequestPart("request") TranslatorRequestDTO request) {
		return translatorService.updateTranslator(id, image, request);
	}

	@DeleteMapping("/deleteTranslator/{id}")
	public ResponseEntity<?> deletTranslator(@PathVariable int id) {
		return translatorService.deleteTranslator(id);
	}

	@GetMapping("/searchTranslator")
	public Page<Translator> searchTranslator(
			@RequestParam(required = false) String searchString,
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int size) {
		return translatorService.searchTranslator(searchString, page, size);
	}

}

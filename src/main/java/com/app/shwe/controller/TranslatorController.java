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
import org.springframework.web.bind.annotation.RestController;

import com.app.shwe.dto.ResponseDTO;
import com.app.shwe.dto.SearchDTO;
import com.app.shwe.dto.TranslatorRequestDTO;
import com.app.shwe.model.Translator;
import com.app.shwe.service.TranslatorService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/translator")
@RequiredArgsConstructor
public class TranslatorController {
	
	@Autowired
	private TranslatorService translatorService;
	
	@PostMapping("/translatorSave")
	public ResponseEntity<ResponseEntity<String>> saveTranslator(@RequestBody TranslatorRequestDTO dto){
		return ResponseEntity.ok(translatorService.saveTranslator(dto));
	}
	
	@GetMapping("/getTransaltorById/{id}")
	public ResponseEntity<Translator> getTranslatorById(@PathVariable int id){
		return translatorService.getTranslatorById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}
	
	@PutMapping("/updateTranslator/{id}")
	public ResponseEntity<ResponseEntity<String>> updateTranslator(@PathVariable int id,@RequestBody TranslatorRequestDTO dto){
		return ResponseEntity.ok(translatorService.updateTranslator(id, dto));
	}
	
	@DeleteMapping("/deleteTranslator/{id}")
	public ResponseEntity<?> deletTranslator(@PathVariable int id){
		return translatorService.deteteTranslator(id);
	}
	
	@GetMapping("/searchTranslator")
	public ResponseEntity<Page<Translator>> searchTranslator(@RequestBody SearchDTO dto){
		return ResponseEntity.ok(translatorService.searchTranslator(dto));
	}

}

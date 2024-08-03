package com.app.shwe.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.shwe.dto.SearchDTO;
import com.app.shwe.dto.TranslatorOrderResponseDTO;
import com.app.shwe.dto.TranslatorRequestDTO;
import com.app.shwe.dto.UserReportDTO;
import com.app.shwe.service.TranslatorService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/translatorOrder")
@RequiredArgsConstructor
public class TranslatorOrderController {

	@Autowired
	private TranslatorService translatorService;
	
	@PostMapping("/hireTranslator")
	public ResponseEntity<ResponseEntity<String>> hireTranslator(@RequestBody TranslatorRequestDTO dto){
		return ResponseEntity.ok(translatorService.hireTranslator(dto));
	}
	
	@GetMapping("/getHireTranslator/{id}")
	public ResponseEntity<Page<TranslatorOrderResponseDTO>> getHireTranslatorById(@RequestBody SearchDTO dto) {
		return ResponseEntity.ok(translatorService.searchHireTranslator( dto));
	}
	
	@PostMapping("/cancelOrder")
	public ResponseEntity<ResponseEntity<String>> cancelOrder(@PathVariable int id,@RequestBody TranslatorRequestDTO dto){
		return ResponseEntity.ok(translatorService.cancelOrder(id,dto));
	}
}

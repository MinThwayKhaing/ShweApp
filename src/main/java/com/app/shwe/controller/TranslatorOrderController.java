package com.app.shwe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.shwe.dto.SearchDTO;
import com.app.shwe.dto.TranslatorOrderRequestDTO;
import com.app.shwe.dto.TranslatorOrderResponseDTO;
import com.app.shwe.dto.TranslatorRequestDTO;
import com.app.shwe.service.TranslatorService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/translatorOrder")
@RequiredArgsConstructor
public class TranslatorOrderController {

	@Autowired
	private TranslatorService translatorService;

	@PostMapping("/hireTranslator")
	public ResponseEntity<String> hireTranslator(@RequestBody TranslatorOrderRequestDTO dto) {
		return translatorService.hireTranslator(dto);
	}

	@GetMapping("/getHireTranslator")
	public Page<TranslatorOrderResponseDTO> getHireTranslator(@RequestBody SearchDTO dto) {
		return translatorService.searchHireTranslator(dto);

	}

	@PutMapping("/cancelOrder/{id}")
	public ResponseEntity<String> cancelOrder(@PathVariable int id) {
		return translatorService.cancelOrder(id);
	}

	@PutMapping("/updateOrder/{id}")
	public ResponseEntity<String> confrimOrder(@PathVariable int id, @RequestBody TranslatorOrderRequestDTO request) {
		return translatorService.updateTranslatorOrder(id, request);
	}

	@PutMapping("/updateOrderFromAdmin/{id}")
	public ResponseEntity<String> confrimOrderFromAdmin(@PathVariable int id,
			@RequestBody TranslatorOrderRequestDTO request) {
		return translatorService.updateTranslatorOrderFromAdmin(id, request);
	}

	// API endpoint to get TranslatorOrder by sysKey
	@GetMapping("/find-by-syskey/{sysKey}")
	public ResponseEntity<?> findOrderBySysKey(@PathVariable String sysKey) {
		return translatorService.findTranslatorOrderBySysKey(sysKey);
	}

	@GetMapping("/getHireTranslatorByUserId")
	public Page<TranslatorOrderResponseDTO> getHireTranslatorByUserId(@RequestBody SearchDTO dto) {
		return translatorService.getHireTranslatorById(dto);
	}

}

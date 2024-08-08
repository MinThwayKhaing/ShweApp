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
	public ResponseEntity<String> hireTranslator(@RequestBody TranslatorRequestDTO dto){
		return translatorService.hireTranslator(dto);
	}
	
	@GetMapping("/getHireTranslator")
	public Page<TranslatorOrderResponseDTO> getHireTranslatorById(@RequestBody SearchDTO dto) {
		return translatorService.searchHireTranslator( dto);
	}
	
	@PostMapping("/cancelOrder/{id}")
	public ResponseEntity<String> cancelOrder(@PathVariable int id){
		return translatorService.cancelOrder(id);
	}
	
	@PostMapping("/updateOrder/{id}")
	public ResponseEntity<String> confrimOrder(@PathVariable int id,@RequestBody TranslatorRequestDTO request){
		return translatorService.updateTranslatorOrder(id,request);
	}
	
	
}

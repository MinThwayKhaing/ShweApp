package com.app.shwe.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.app.shwe.dto.Report90DayRequestDTO;
import com.app.shwe.dto.Report90ResponseDTO;
import com.app.shwe.dto.Tm30ResponseDTO;
import com.app.shwe.service.Report90DayService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/report90day")
@RequiredArgsConstructor
public class Report90DayController {

	@Autowired
	private Report90DayService reportService;

	@PostMapping("/save90DayReport")
	public ResponseEntity<String> saveTm30(@RequestPart("tm6Photo") MultipartFile tm6Photo,
			@RequestPart("expireDatePhoto") MultipartFile expireDatePhoto,
			@RequestPart("passportBio") MultipartFile passportBio, @RequestPart("visaPage") MultipartFile visaPage,
			@RequestPart("request") Report90DayRequestDTO request) {
		return reportService.saveReport90Day(tm6Photo, expireDatePhoto, passportBio, visaPage, request);

	}

	// @GetMapping("/getReport90DayOrder")
	// public List<Report90ResponseDTO> getTm30OrderByuserId() {
	// return reportService.getReport90DayOrder();
	// }

	@PutMapping("/cancelOrder/{id}")
	public ResponseEntity<String> cancelOrder(@PathVariable int id) {
		return reportService.cancelOrder(id);
	}

}

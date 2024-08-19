package com.app.shwe.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.shwe.dto.Report90DayTypeRequestDTO;
import com.app.shwe.dto.Report90DayTypeResponseType;
import com.app.shwe.dto.VisaTypeRequestDTO;
import com.app.shwe.dto.VisaTypeResponseDTO;
import com.app.shwe.service.Report90DayVisaTypeService;
import com.app.shwe.service.VisaTypeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/report-visa-type")
@RequiredArgsConstructor
public class Report90DayTypeController {
	
	@Autowired
    private Report90DayVisaTypeService visaTypeService;

    @PostMapping("/save90DayReportType")
	public ResponseEntity<String> save90DayReportTypes(@RequestBody Report90DayTypeRequestDTO request) {
		return visaTypeService.saveVisaType(request);
	}

    @GetMapping("/get90DayReportType")
	public List<Report90DayTypeResponseType> getNewsById(@RequestBody Report90DayTypeRequestDTO request) {
		return visaTypeService.getVisaByType(request);
	}
    
    @PutMapping("/update90DayReportType/{id}")
	public ResponseEntity<String> updateVisa(@PathVariable int id,@RequestBody Report90DayTypeRequestDTO request) {
		return visaTypeService.updateVisaType(id,request);
	}
    
    @GetMapping("/getAll90DayReportType")
   	public List<Report90DayTypeResponseType> getNewsById() {
   		return visaTypeService.getAllVisaType();
   	}
    
    @DeleteMapping("/delete90DayReportType/{id}")
	public ResponseEntity<?> deleteVisaType(@PathVariable int id) {
		return visaTypeService.deleteVisaType(id);
	}
    

}

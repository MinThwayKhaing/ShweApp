package com.app.shwe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.app.shwe.dto.VisaServiceRequestDTO;
import com.app.shwe.dto.VisaTypeRequestDTO;
import com.app.shwe.service.VisaTypeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/visa-type")
@RequiredArgsConstructor
public class VisaTypeController {
    
    @Autowired
    private VisaTypeService visaTypeService;

    @PostMapping("/saveVisaType")
	public ResponseEntity<String> saveVisaTypes(@RequestBody VisaTypeRequestDTO request) {
		return visaTypeService.saveVisaType(request);

	}

}

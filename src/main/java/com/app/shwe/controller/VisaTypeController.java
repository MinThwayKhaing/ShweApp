package com.app.shwe.controller;

import java.util.List;

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

import com.app.shwe.dto.VisaServiceRequestDTO;
import com.app.shwe.dto.VisaTypeRequestDTO;
import com.app.shwe.dto.VisaTypeResponseDTO;
import com.app.shwe.model.VisaServices;
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

    @GetMapping("/getVisaByType")
	public List<VisaTypeResponseDTO> getNewsById(@RequestBody VisaTypeRequestDTO request) {
		return visaTypeService.getVisaByType(request);
	}

}

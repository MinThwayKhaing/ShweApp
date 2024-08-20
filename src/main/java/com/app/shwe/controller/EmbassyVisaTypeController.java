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

import com.app.shwe.dto.EmbassyVisaTypeRequestDTO;
import com.app.shwe.dto.EmbassyVisaTypeResponseDTO;
import com.app.shwe.service.EmbassyVisaTypeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/embassy-visa-type")
@RequiredArgsConstructor
public class EmbassyVisaTypeController {
	
	 @Autowired
	    private EmbassyVisaTypeService visaTypeService;

	    @PostMapping("/saveVisaType")
		public ResponseEntity<String> saveVisaTypes(@RequestBody EmbassyVisaTypeRequestDTO request) {
			return visaTypeService.saveVisaType(request);
		}

		@GetMapping("/getVisaByType")
		public List<EmbassyVisaTypeResponseDTO> getVisaExtensionType(@RequestBody EmbassyVisaTypeRequestDTO request) {
			return visaTypeService.getVisaByType(request);
		}

		@PutMapping("/updateVisaType/{id}")
		public ResponseEntity<String> updateVisaExtensionType(@PathVariable int id,@RequestBody EmbassyVisaTypeRequestDTO request) {
			return visaTypeService.updateVisaType(id,request);
		}
	    
	    @GetMapping("/getAllVisaType")
	   	public List<EmbassyVisaTypeResponseDTO> getVisaExtensionType() {
	   		return visaTypeService.getAllVisaType();
	   	}
	    
	    @DeleteMapping("/deleteVisaType/{id}")
		public ResponseEntity<?> deleteVisaType(@PathVariable int id) {
			return visaTypeService.deleteVisaType(id);
		}

}

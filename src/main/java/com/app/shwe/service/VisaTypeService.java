package com.app.shwe.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.app.shwe.dto.VisaServiceRequestDTO;
import com.app.shwe.dto.VisaTypeRequestDTO;
import com.app.shwe.model.SubVisaType;
import com.app.shwe.model.VisaServices;
import com.app.shwe.model.VisaType;
import com.app.shwe.repository.SubVisaTypeRepository;
import com.app.shwe.repository.UserRepository;
import com.app.shwe.repository.VsiaTypeRepository;
import com.app.shwe.utils.SecurityUtils;

import jakarta.transaction.Transactional;

@Service
public class VisaTypeService {

    @Autowired
    private VsiaTypeRepository vsiaTypeRepository;

    @Autowired
    private SubVisaTypeRepository subVisaTypeRepository;

    @Autowired
    private UserRepository userRepository;


    @Transactional
	public ResponseEntity<String> saveVisaType( VisaTypeRequestDTO request) {
		if (request == null) {
			throw new IllegalArgumentException("Request and required fields must not be null");
		}

		try {
            VisaType visaType = new VisaType();
            visaType.setVisaType(request.getVisaType());
            visaType.setCreatedBy(userRepository.authUser(SecurityUtils.getCurrentUsername()));
            visaType.setCreatedDate(new Date());
            vsiaTypeRepository.save(visaType);
            SubVisaType subVisaType = new SubVisaType();
            subVisaType.setPrice(request.getPrice());
            subVisaType.setDuration(request.getDuration());
            subVisaType.setCreatedBy(userRepository.authUser(SecurityUtils.getCurrentUsername()));
            subVisaType.setCreatedDate(new Date());
            subVisaType.setVisa(visaType); 
            subVisaTypeRepository.save(subVisaType);
		
			
			return ResponseEntity.status(HttpStatus.OK).body("Visa saved successfully.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error occurred while saving news: " + e.getMessage());
		}

	}
    
}

package com.app.shwe.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.app.shwe.dto.VisaTypeRequestDTO;
import com.app.shwe.dto.VisaTypeResponseDTO;
import com.app.shwe.model.SubVisaType;
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
    
    @Transactional
	public List<VisaTypeResponseDTO> getVisaByType(VisaTypeRequestDTO request) {
		if (request == null) {
			throw new IllegalArgumentException("Request and required fields must not be null");
		}
        List<VisaTypeResponseDTO> visaList = vsiaTypeRepository.findVisaByType(request.getVisaType());
		List<VisaTypeResponseDTO> response = new ArrayList<>();
        for (VisaTypeResponseDTO visaTypeResponseDTO : visaList) {
            VisaTypeResponseDTO dto = new VisaTypeResponseDTO();
            VisaType type = new VisaType();
            type.setVisaType(dto.getVisaType());
            dto.setVisaType(visaTypeResponseDTO.getVisaType());
            SubVisaType sub = new SubVisaType();
            sub.setDuration(dto.getDuration());
            sub.setPrice(dto.getPrice());
            dto.setDuration(visaTypeResponseDTO.getDuration());
            dto.setPrice(visaTypeResponseDTO.getPrice());
            response.add(visaTypeResponseDTO);
            
        }

		return response;
	}
}

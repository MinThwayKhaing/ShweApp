package com.app.shwe.service;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.app.shwe.dto.TermsAndConditionDTO;
import com.app.shwe.dto.TranslatorRequestDTO;
import com.app.shwe.model.TermsAndCondition;
import com.app.shwe.model.Tm30;
import com.app.shwe.model.Translator;
import com.app.shwe.repository.TermsAndConditionRepository;
import com.app.shwe.repository.UserRepository;
import com.app.shwe.utils.SecurityUtils;

import jakarta.transaction.Transactional;

@Service
public class TermsAndConditionService {
	
	@Autowired
	private TermsAndConditionRepository termsAndConditonRepo;
	
	@Autowired
	private UserRepository userRepository;
	
	public ResponseEntity<String> saveTermsAndCondition(TermsAndConditionDTO request) {
		try {
			if (request == null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Request data is null.");
			}
			TermsAndCondition terms = new TermsAndCondition();
			terms.setDescription(request.getDescription());
			terms.setCreatedDate(new Date());
			terms.setCreatedBy(userRepository.authUser(SecurityUtils.getCurrentUsername()));
			termsAndConditonRepo.save(terms);
			return ResponseEntity.status(HttpStatus.OK).body("Terms and condition saved successfully.");

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error occurred while saving translator: " + e.getMessage());
		}

	}
	
	@Transactional
	public TermsAndCondition getTermsAndCondition() {
		
		TermsAndCondition tnc = termsAndConditonRepo.getTermsAndCondition();
		return tnc;
	}

}

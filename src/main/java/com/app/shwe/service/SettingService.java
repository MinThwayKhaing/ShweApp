package com.app.shwe.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.app.shwe.dto.SettingResponseDTO;
import com.app.shwe.model.Article;
import com.app.shwe.model.TermsAndCondition;
import com.app.shwe.model.VisaServices;
import com.app.shwe.repository.ArticleRepository;
import com.app.shwe.repository.TermsAndConditionRepository;
import com.app.shwe.repository.VisaServicesRepository;

@Service
public class SettingService {

	@Autowired
	private VisaServicesRepository visaRepo;

	@Autowired
	private ArticleRepository articleRepo;

	@Autowired
	private TermsAndConditionRepository termsAndConditonRepo;

	public ResponseEntity<?> getAllSetting() {
		try {
			// Fetching data from repositories
			TermsAndCondition tnc = termsAndConditonRepo.getTermsAndCondition();
			List<VisaServices> visaList = visaRepo.findAll();
			List<Article> articleList = articleRepo.findAll();

			// Creating the response DTO
			SettingResponseDTO response = new SettingResponseDTO();
			response.setTnc(tnc);
			response.setVisa_services(visaList);
			response.setArticle(articleList);

			// Returning the response entity with HTTP status 200 (OK)
			return ResponseEntity.ok(response);

		} catch (Exception e) {
			// Logging the exception (optional)
			e.printStackTrace();

			// Creating an error response with HTTP status 500 (Internal Server Error)
			String errorMessage = "Failed to retrieve settings due to an internal error.";
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
		}
	}
}

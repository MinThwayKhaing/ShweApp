package com.app.shwe.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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

	public SettingResponseDTO getAllSetting() {
		TermsAndCondition tnc = termsAndConditonRepo.getTermsAndCondition();
		List<VisaServices> visaList = visaRepo.findAll();
		List<Article> articleList = articleRepo.findAll();

		SettingResponseDTO response = new SettingResponseDTO();

		response.setTnc(tnc);
		response.setVisa_services(visaList);
		response.setArticle(articleList);
		return response;
	}
}

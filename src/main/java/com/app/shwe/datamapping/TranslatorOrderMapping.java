package com.app.shwe.datamapping;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.shwe.dto.TranslatorOrderResponseDTO;
import com.app.shwe.dto.TranslatorRequestDTO;
import com.app.shwe.model.Translator;
import com.app.shwe.model.TranslatorOrder;
import com.app.shwe.repository.TranslatorOrderRepostitory;
import com.app.shwe.repository.TranslatorRepository;
import com.app.shwe.repository.UserRepository;
import com.app.shwe.utils.SecurityUtils;

@Service
public class TranslatorOrderMapping {
	
	 @Autowired
	 private UserRepository userRepository;
	
	@Autowired
	private TranslatorRepository translatorRepository;
	

	 public TranslatorOrder mapToTranslatorOrder(TranslatorRequestDTO dto) {
	    	TranslatorOrder order = new TranslatorOrder();
	    	Translator translator = translatorRepository.findById(dto.getTranslator_id())
	                .orElseThrow(() -> new RuntimeException("CarRent not found for ID: " + dto.getTranslator_id()));
	    	order.setStatus("Pending");
	    	order.setCreatedBy(userRepository.authUser(SecurityUtils.getCurrentUsername()));
	    	order.setCreatedDate(new Date());
	    	order.setTranslator(translator);
	    	return order;
	    }
	 
	 
}
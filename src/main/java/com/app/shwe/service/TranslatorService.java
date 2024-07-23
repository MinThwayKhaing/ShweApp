package com.app.shwe.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.app.shwe.dto.ResponseDTO;
import com.app.shwe.dto.SearchDTO;
import com.app.shwe.dto.TranslatorRequestDTO;
import com.app.shwe.model.Translator;
import com.app.shwe.userRepository.TranslatorRepository;

@Service
public class TranslatorService {
	
	@Autowired
	private TranslatorRepository translatorRepo;
	
	public ResponseDTO saveTranslator(TranslatorRequestDTO dto) {
		ResponseDTO response = new ResponseDTO();
		if(!dto.getName().isEmpty() && !dto.getLanguage().isEmpty() && dto.getSpecialist() != null) {
			Translator translator = new Translator();
			translator.setName(dto.getName());
			translator.setLanguage(dto.getLanguage());
			translator.setSpecialist(dto.getSpecialist());
			translatorRepo.save(translator);
			response.setStatus(200);
			response.setDescription("Translator save successfully");
		}else {
			response.setStatus(403);
			response.setDescription("Translator save fail");
		}
		
		return response;
		
	}
	
	public Optional<Translator> getTranslatorById(int id){
		Optional<Translator> translator = translatorRepo.findById(id);
		if(translator.isEmpty() || translator.get() == null) {
			return Optional.empty();
		}
		return translator;
	}
	
	public ResponseDTO updateDTO(int id, TranslatorRequestDTO dto) {
	    ResponseDTO response = new ResponseDTO();
	    Optional<Translator> getTranslator = translatorRepo.findById(id);
	    if(getTranslator.isPresent()) {
	        Translator translator = getTranslator.get();
	        translator.setName(dto.getName());
	        translator.setLanguage(dto.getLanguage());
	        translator.setSpecialist(dto.getSpecialist());
	        translatorRepo.save(translator); // Save the updated translator
	        response.setStatus(200);
	        response.setDescription("Update Translator successfully");
	    } else {
	        response.setStatus(403);
	        response.setDescription("Update Translator fail");
	    }
	    return response;
	}
	
	
	public ResponseDTO deteteTranslator(int id) {
		ResponseDTO response = new ResponseDTO();
		translatorRepo.deleteById(id);
		response.setStatus(200);
		response.setDescription("Delete translator successfully");
		return response;
	}

	public Page<Translator> searchTranslator(SearchDTO dto){
		String searchString = dto.getSearchString();
		int page = (dto.getPage() < 1) ? 0 : dto.getPage() - 1;
		int size = dto.getSize();
		Pageable pageable = PageRequest.of(page, size);
		return translatorRepo.searchTranslator(searchString, pageable);
		
	}


}

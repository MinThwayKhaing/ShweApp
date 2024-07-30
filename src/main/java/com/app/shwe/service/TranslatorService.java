package com.app.shwe.service;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.app.shwe.dto.ResponseDTO;
import com.app.shwe.dto.SearchDTO;
import com.app.shwe.dto.TranslatorRequestDTO;
import com.app.shwe.model.Translator;
import com.app.shwe.repository.TranslatorRepository;
import com.app.shwe.utils.SecurityUtils;

@Service
public class TranslatorService {

	@Autowired
	private TranslatorRepository translatorRepo;

	public ResponseEntity<String> saveTranslator(TranslatorRequestDTO dto) {
		try {
			if (dto == null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Request data is null.");
			}
			Translator translator = new Translator();
			translator.setName(dto.getName());
			translator.setLanguage(dto.getLanguage());
			translator.setSpecialist(dto.getSpecialist());
			translator.setCreatedDate(new Date());
			translator.setCreatedBy(SecurityUtils.getCurrentUsername());
			translatorRepo.save(translator);
			return ResponseEntity.status(HttpStatus.OK).body("Translator saved successfully.");

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error occurred while saving translator: " + e.getMessage());
		}

	}

	public Optional<Translator> getTranslatorById(Integer id) {
		if (id == null) {
			throw new IllegalArgumentException("Id cannot be null");
		}
		Optional<Translator> translator = translatorRepo.findById(id);
		return translator;
	}

	public ResponseEntity<String> updateTranslator(int id, TranslatorRequestDTO dto) {
		Optional<Translator> getTranslator = translatorRepo.findById(id);
		if (!getTranslator.isPresent()) {
			throw new IllegalArgumentException("ID not found");
		}
		try {
			Translator translator = getTranslator.get();
			translator.setName(dto.getName());
			translator.setLanguage(dto.getLanguage());
			translator.setSpecialist(dto.getSpecialist());
			translatorRepo.save(translator); // Save the updated translator
			return new ResponseEntity<>("CarOrder updated successfully", HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>("Error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	public ResponseEntity<?> deteteTranslator(int id) {
		int count = translatorRepo.checkTranslator(id);
		if (count == 0) {
			return new ResponseEntity<>("Translator with ID " + id + " not found", HttpStatus.NOT_FOUND);
		}
		try {
			translatorRepo.deleteById(id);
			return new ResponseEntity<>("Translator deleted successfully", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}

	public Page<Translator> searchTranslator(SearchDTO dto) {
		String searchString = dto.getSearchString();
		int page = (dto.getPage() < 1) ? 0 : dto.getPage() - 1;
		int size = dto.getSize();
		Pageable pageable = PageRequest.of(page, size);
		return translatorRepo.searchTranslator(searchString, pageable);

	}

}

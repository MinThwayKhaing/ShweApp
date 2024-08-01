package com.app.shwe.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.app.shwe.datamapping.TranslatorOrderMapping;
import com.app.shwe.dto.ResponseDTO;
import com.app.shwe.dto.SearchDTO;
import com.app.shwe.dto.TranslatorOrderResponseDTO;
import com.app.shwe.dto.TranslatorRequestDTO;
import com.app.shwe.model.Translator;
import com.app.shwe.model.TranslatorOrder;
import com.app.shwe.repository.TranslatorOrderRepostitory;
import com.app.shwe.repository.TranslatorRepository;
import com.app.shwe.repository.UserRepository;
import com.app.shwe.utils.FilesSerializationUtil;
import com.app.shwe.utils.SecurityUtils;

import jakarta.transaction.Transactional;

@Service
public class TranslatorService {

	@Autowired
	private TranslatorRepository translatorRepo;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private FileUploadService fileUploadService;

	@Autowired
	private TranslatorOrderMapping orderMapping;

	@Autowired
	private TranslatorOrderRepostitory transOrderRepository;

	@Autowired
	private UserRepository userRepo;

	public ResponseEntity<String> saveTranslator(MultipartFile image,TranslatorRequestDTO request) {
		try {
			if (request == null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Request data is null.");
			}
			String imageUrl = fileUploadService.uploadFile(image);
			Translator translator = new Translator();
			translator.setImage(imageUrl);
			translator.setName(request.getName());
			translator.setLanguage(request.getLanguage());
			translator.setSpecialist(request.getSpecialist());
			translator.setCreatedDate(new Date());
			translator.setCreatedBy(userRepository.authUser(SecurityUtils.getCurrentUsername()));
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

	public ResponseEntity<String> updateTranslator(int id, MultipartFile image, TranslatorRequestDTO request) {
		Optional<Translator> getTranslator = translatorRepo.findById(id);
		if (!getTranslator.isPresent()) {
			throw new IllegalArgumentException("ID not found");
		}
		try {
			Translator translator = getTranslator.get();
			if (image != null && !image.isEmpty()) {
				String imageUrl = fileUploadService.uploadFile(image);
				translator.setImage(imageUrl);
			}
			translator.setName(request.getName());
			translator.setLanguage(request.getLanguage());
			translator.setSpecialist(request.getSpecialist());
			translatorRepo.save(translator); // Save the updated translator
			return new ResponseEntity<>("Translator updated successfully", HttpStatus.OK);

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

	@Transactional
	public ResponseEntity<String> hireTranslator(TranslatorRequestDTO dto) {
		if (dto == null) {
			throw new IllegalArgumentException("Request and required fields must not be null");
		}
		dto.setStatus("Pending");
		try {
			TranslatorOrder order = orderMapping.mapToTranslatorOrder(dto);
			transOrderRepository.save(order);
			return ResponseEntity.status(HttpStatus.OK).body("Translator order saved successfully.");
		} catch (Exception e) {
			return new ResponseEntity<>("Error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public Page<TranslatorOrderResponseDTO> searchHireTranslator(int id, SearchDTO dto) {
		String searchString = dto.getSearchString();
		int page = (dto.getPage() < 1) ? 0 : dto.getPage() - 1;
		int size = dto.getSize();
		Pageable pageable = PageRequest.of(page, size);
		return transOrderRepository.searchHireTranslator(id, searchString, pageable);
	}

	public ResponseEntity<String> cancelOrder(int orderId, TranslatorRequestDTO dto) {
		if (dto.getStatus().equals("")) {
			throw new IllegalArgumentException("Request and required fields must not be null");
		}
		try {
			String status = dto.getStatus();
			transOrderRepository.cancelOrder(orderId, status);
			return ResponseEntity.status(HttpStatus.OK).body("Cancel translator order successfully.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error occurred while saving translator: " + e.getMessage());
		}
	}

}

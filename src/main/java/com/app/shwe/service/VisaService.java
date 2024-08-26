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

import com.app.shwe.dto.SearchDTO;
import com.app.shwe.dto.VisaServiceRequestDTO;
import com.app.shwe.model.VisaServices;
import com.app.shwe.repository.UserRepository;
import com.app.shwe.repository.VisaServicesRepository;
import com.app.shwe.utils.SecurityUtils;

import jakarta.transaction.Transactional;

@Service
public class VisaService {

	@Autowired
	private FileUploadService fileUploadService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private VisaServicesRepository visaRepo;

	@Transactional
	public ResponseEntity<String> saveVisa(MultipartFile images, VisaServiceRequestDTO request) {
		if (images == null && request == null) {
			throw new IllegalArgumentException("Request and required fields must not be null");
		}

		try {
			String imageUrl = fileUploadService.uploadFile(images);
			VisaServices visa = new VisaServices();
			visa.setServiceName(request.getServiceName());
			visa.setImage(imageUrl);
			visa.setCreatedBy(userRepository.authUser(SecurityUtils.getCurrentUsername()));
			visa.setCreatedDate(new Date());
			visaRepo.save(visa);
			return ResponseEntity.status(HttpStatus.OK).body("Visa saved successfully.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error occurred while saving news: " + e.getMessage());
		}

	}

	@Transactional
	public Optional<VisaServices> getVisaById(Integer id) {
		if (id == null) {
			throw new IllegalArgumentException("Id cannot be null");
		}
		Optional<VisaServices> visa = visaRepo.findById(id);
		return visa;
	}

	@Transactional
	public Page<VisaServices> getAllVisa(SearchDTO dto) {
		String search = dto.getSearchString();
		int page = (dto.getPage() < 1) ? 0 : dto.getPage() - 1;
		int size = dto.getSize();
		Pageable pageable = PageRequest.of(page, size);
		return visaRepo.getAllVisa(search, pageable);
	}

	@Transactional
	public ResponseEntity<String> updateVisaService(int id, MultipartFile image, VisaServiceRequestDTO request) {
		Optional<VisaServices> getVisa = visaRepo.findById(id);
		if (!getVisa.isPresent()) {
			throw new IllegalArgumentException("ID not found");
		}

		try {
			VisaServices visa = getVisa.get();
			String imageUrl = fileUploadService.uploadFile(image);
			visa.setServiceName(request.getServiceName());
			visa.setImage(imageUrl);
			visa.setUpdatedBy(userRepository.authUser(SecurityUtils.getCurrentUsername()));
			visa.setUpdatedDate(new Date());
			visaRepo.save(visa);
			return new ResponseEntity<>("Visa updated successfully", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	@Transactional
	public ResponseEntity<?> deleteVisa(int id) {
		int count = visaRepo.checkVisaById(id);

		if (count == 0) {
			return new ResponseEntity<>("Visa with ID " + id + " not found", HttpStatus.NOT_FOUND);
		}

		try {
			visaRepo.deleteById(id);
			return new ResponseEntity<>("Visa deleted successfully", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	public List<VisaServices> getAllVisaService(){
		List<VisaServices> visaList = visaRepo.getAllVisaService();
		List<VisaServices> visa = new ArrayList<>();
		for (VisaServices visaService : visaList) {
			visa.add(visaService);
		}
		return visaList;
	}
	

	

}

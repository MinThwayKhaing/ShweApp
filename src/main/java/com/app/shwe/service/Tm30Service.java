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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.app.shwe.dto.NewsRequestDTO;
import com.app.shwe.dto.SearchDTO;
import com.app.shwe.dto.Tm30RequestDTO;
import com.app.shwe.dto.Tm30ResponseDTO;
import com.app.shwe.dto.VisaResponseDTO;
import com.app.shwe.model.CarRent;
import com.app.shwe.model.News;
import com.app.shwe.model.Tm30;
import com.app.shwe.model.User;
import com.app.shwe.model.VisaOrder;
import com.app.shwe.model.VisaServices;
import com.app.shwe.repository.OrderKeyGeneratorService;
import com.app.shwe.repository.Tm30Repository;
import com.app.shwe.repository.UserRepository;
import com.app.shwe.repository.VisaOrderRepository;
import com.app.shwe.repository.VisaServicesRepository;
import com.app.shwe.utils.SecurityUtils;

import jakarta.transaction.Transactional;

@Service
public class Tm30Service {

	@Autowired
	private Tm30Repository tm30Repo;

	@Autowired
	private FileUploadService fileUploadService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private VisaServicesRepository visaRepo;
	
	@Autowired
	private OrderKeyGeneratorService keyGenerator;
	
	@Autowired
	private VisaOrderRepository visaOrderRepository;

	@Transactional
	public ResponseEntity<String> saveTm30(MultipartFile passport, MultipartFile visa, Tm30RequestDTO request) {
		if (passport == null && visa == null && request == null) {
			throw new IllegalArgumentException("Request and required fields must not be null");
		}
		try {
			int userId = userRepository.authUser(SecurityUtils.getCurrentUsername());
			User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found for ID: " + userId));
			
			VisaServices visaServices = visaRepo.findById(request.getVisa_id())
					.orElseThrow(() -> new IllegalArgumentException("Visa not found with id: " + request.getVisa_id()));

			String imageUrl1 = fileUploadService.uploadFile(passport);
			String imageUrl2 = fileUploadService.uploadFile(visa);
			Tm30 tm30 = new Tm30();
			VisaOrder order = new VisaOrder();
			tm30.setPeriod(request.getPeriod());
			tm30.setPassportBio(imageUrl1);
			tm30.setVisaPage(imageUrl2);
			tm30.setContactNumber(request.getContactNumber());
			tm30.setDuration(request.getDuration());
			tm30.setVisa(visaServices);
			tm30.setCreatedBy(userRepository.authUser(SecurityUtils.getCurrentUsername()));
			tm30.setCreatedDate(new Date());
			tm30.setUser(user);
			tm30Repo.save(tm30);
			order.setOrder_id(keyGenerator.generateNextCarOrderId());
			
			if(request.getOption1() == 1) {
				order.setSub_visa_id(request.getOption1());
				order.setMain_visa_id(request.getVisa_id());
			}
			if(request.getOption2() == 2) {
				order.setSub_visa_id(request.getOption2());
				order.setMain_visa_id(request.getVisa_id());
			}
			if(request.getOption3() == 3) {
				order.setSub_visa_id(request.getOption3());
				order.setMain_visa_id(request.getVisa_id());
			}
			visaOrderRepository.save(order);
			return ResponseEntity.status(HttpStatus.OK).body("Tm-30 saved successfully.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error occurred while saving TM-30: " + e.getMessage());
		}
	}

	@Transactional
	public Optional<Tm30> getTm30ById(Integer id) {
		if (id == null) {
			throw new IllegalArgumentException("Id cannot be null");
		}
		Optional<Tm30> tm30 = tm30Repo.findById(id);
		return tm30;
	}

	@Transactional
	public Page<Tm30> getTm30(SearchDTO search) {
		int page = (search.getPage() < 1) ? 0 : search.getPage() - 1;
		int size = search.getSize();
		Pageable pageable = PageRequest.of(page, size);
		return tm30Repo.getAllTm30(pageable);
	}
	
	@Transactional
	public Page<VisaResponseDTO> getAllTm30(SearchDTO search) {
		int page = (search.getPage() < 1) ? 0 : search.getPage() - 1;
		int size = search.getSize();
		Tm30ResponseDTO dto = new Tm30ResponseDTO();
		Pageable pageable = PageRequest.of(page, size);
		Page<Tm30> tm30 = tm30Repo.getAllTm30(pageable);
		List<VisaResponseDTO> response = new ArrayList<VisaResponseDTO>(); 
		for (Tm30 tm : tm30) {
			VisaResponseDTO visa = new VisaResponseDTO();
			visa = tm30Repo.getTM30(tm.getId());
			response.add(visa);
		}
		dto.setVisa(response);
		return dto;
	}


	@Transactional
	public ResponseEntity<String> updateTm30(int id, MultipartFile passportPage, MultipartFile visaPage,
			Tm30RequestDTO request) {
		Optional<Tm30> getTm30 = tm30Repo.findById(id);
		if (!getTm30.isPresent()) {
			throw new IllegalArgumentException("ID not found");
		}

		try {
			Tm30 tm30 = getTm30.get();
			String passportImage = fileUploadService.uploadFile(passportPage);
			String visaImage = fileUploadService.uploadFile(visaPage);
			tm30.setPeriod(request.getPeriod());
			tm30.setContactNumber(request.getContactNumber());
			tm30.setDuration(request.getDuration());
			tm30.setPassportBio(passportImage);
			tm30.setVisaPage(visaImage);
			tm30.setUpdatedBy(userRepository.authUser(SecurityUtils.getCurrentUsername()));
			tm30.setUpdatedDate(new Date());
			tm30Repo.save(tm30);
			return new ResponseEntity<>("Tm-30 updated successfully", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	
	@Transactional
	public ResponseEntity<?> deleteTm30(int id) {
		int count = tm30Repo.checkTm30ById(id);

		if (count == 0) {
			return new ResponseEntity<>("Tm-30 with ID " + id + " not found", HttpStatus.NOT_FOUND);
		}

		try {
			tm30Repo.deleteById(id);
			return new ResponseEntity<>("Tm-30 deleted successfully", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}

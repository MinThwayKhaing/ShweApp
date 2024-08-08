package com.app.shwe.service;

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
import com.app.shwe.model.News;
import com.app.shwe.model.Tm30;
import com.app.shwe.model.VisaServices;
import com.app.shwe.repository.Tm30Repository;
import com.app.shwe.repository.UserRepository;
import com.app.shwe.repository.VisaServicesRepository;
import com.app.shwe.utils.DurationConfig;
import com.app.shwe.utils.FilesSerializationUtil;
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
	private DurationConfig durationConfig;

	@Autowired
	private VisaServicesRepository visaRepo;

	@Transactional
	public ResponseEntity<String> saveTm30(MultipartFile passport, MultipartFile visa, Tm30RequestDTO request) {
		if (passport == null && visa == null && request == null) {
			throw new IllegalArgumentException("Request and required fields must not be null");
		}
		try {
			List<String> validDurations = durationConfig.durationList();
			String requestDuration = request.getDuration();
			if (!validDurations.contains(requestDuration)) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body("Invalid duration provided: " + requestDuration);
			}
			VisaServices visaServices = visaRepo.findById(request.getVisa_id())
					.orElseThrow(() -> new IllegalArgumentException("Visa not found with id: " + request.getVisa_id()));

			String imageUrl1 = fileUploadService.uploadFile(passport);
			String imageUrl2 = fileUploadService.uploadFile(visa);
			Tm30 tm30 = new Tm30();
			tm30.setPeriod(request.getPeriod());
			tm30.setPassportBio(imageUrl1);
			tm30.setVisaPage(imageUrl2);
			tm30.setContactNumber(request.getContactNumber());
			tm30.setDuration(requestDuration);
			tm30.setVisa(visaServices);
			tm30.setCreatedBy(userRepository.authUser(SecurityUtils.getCurrentUsername()));
			tm30.setCreatedDate(new Date());
			tm30Repo.save(tm30);
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
	public Page<Tm30> getAllTm30(SearchDTO search) {
		int page = (search.getPage() < 1) ? 0 : search.getPage() - 1;
		int size = search.getSize();
		Pageable pageable = PageRequest.of(page, size);
		return tm30Repo.getAllTm30(pageable);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@Transactional
	public ResponseEntity<String> updateTm30(int id, MultipartFile passportPage, MultipartFile visaPage,
			Tm30RequestDTO request) {
		Optional<Tm30> getTm30 = tm30Repo.findById(id);
		if (!getTm30.isPresent()) {
			throw new IllegalArgumentException("ID not found");
		}

		try {
			Tm30 tm30 = getTm30.get();
			List<String> validDurations = durationConfig.durationList();
			String requestDuration = request.getDuration();
			if (!validDurations.contains(requestDuration)) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body("Invalid duration provided: " + requestDuration);
			}
			String passportImage = fileUploadService.uploadFile(passportPage);
			String visaImage = fileUploadService.uploadFile(visaPage);
			tm30.setPeriod(request.getPeriod());
			tm30.setContactNumber(request.getContactNumber());
			tm30.setDuration(requestDuration);
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

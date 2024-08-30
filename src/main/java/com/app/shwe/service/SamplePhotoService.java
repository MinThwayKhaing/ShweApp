package com.app.shwe.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.app.shwe.dto.SamplePhotoRequest;
import com.app.shwe.model.SamplePhoto;
import com.app.shwe.repository.SamplePhotoRepository;
import com.app.shwe.repository.UserRepository;
import com.app.shwe.utils.SecurityUtils;

import jakarta.transaction.Transactional;

@Service
public class SamplePhotoService {

	@Autowired
	private SamplePhotoRepository sampleRepo;

	@Autowired
	private FileUploadService fileUploadService;

	@Autowired
	private UserRepository userRepository;

	// @PreAuthorize("hasRole('ADMIN')")
	@Transactional
	public ResponseEntity<String> saveSamplePhoto(MultipartFile image, SamplePhotoRequest request) {
		if (image == null && request == null) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error occurred while saving sample photo: ");
		}
		try {
			String imageUrl = fileUploadService.uploadFile(image);
			SamplePhoto sample = new SamplePhoto();
			sample.setTitle(request.getTitle());
			sample.setPhoto(imageUrl);
			sample.setCreatedBy(userRepository.authUser(SecurityUtils.getCurrentUsername()));
			sample.setCreatedDate(new Date());
			sampleRepo.save(sample);
			return ResponseEntity.status(HttpStatus.OK).body("Sample photo saved successfully.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error occurred while saving news: " + e.getMessage());
		}

	}

	@PreAuthorize("hasRole('ADMIN')")
	@Transactional
	public ResponseEntity<String> updateSamplePhoto(int id, MultipartFile image, SamplePhotoRequest request) {
		Optional<SamplePhoto> getSamplePhoto = sampleRepo.findById(id);
		if (!getSamplePhoto.isPresent()) {
			throw new IllegalArgumentException("ID not found");
		}

		try {
			SamplePhoto photo = getSamplePhoto.get();
			if (image != null && !image.isEmpty()) {
				String imageUrl = fileUploadService.uploadFile(image);
				photo.setPhoto(imageUrl);
			}
			photo.setTitle(request.getTitle());
			photo.setUpdatedBy(userRepository.authUser(SecurityUtils.getCurrentUsername()));
			photo.setUpdatedDate(new Date());
			sampleRepo.save(photo);
			return new ResponseEntity<>("Sample Photo updated successfully", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@Transactional
	public Optional<SamplePhoto> getSamplePhotoById(Integer id) {
		if (id == null) {
			throw new IllegalArgumentException("Id cannot be null");
		}
		Optional<SamplePhoto> photo = sampleRepo.findById(id);
		return photo;
	}

	@PreAuthorize("hasRole('ADMIN')")
	@Transactional
	public ResponseEntity<?> deleteSamplePhoto(int id) {
		int count = sampleRepo.checkExistOrNot(id);

		if (count == 0) {
			return new ResponseEntity<>("Sample photo with ID " + id + " not found", HttpStatus.NOT_FOUND);
		}

		try {
			sampleRepo.deleteById(id);
			return new ResponseEntity<>("Sample photo deleted successfully", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public List<SamplePhoto> getAllSamplePhoto() {
		List<SamplePhoto> customPhotoList = sampleRepo.findCustomSamplePhotos();
		List<SamplePhoto> processedPhotoList = new ArrayList<>();

		for (SamplePhoto photo : customPhotoList) {
			processedPhotoList.add(photo); 
		}

		return processedPhotoList;
	}

//	public Page<News> getAllNewsByDate(SearchDTO search) {
//		int page = (search.getPage() < 1) ? 0 : search.getPage() - 1;
//		int size = search.getSize();
//		Pageable pageable = PageRequest.of(page, size);
//		return newsRepository.getAllNewsByDate(pageable);
//	}

}

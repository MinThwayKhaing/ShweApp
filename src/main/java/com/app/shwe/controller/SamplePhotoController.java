package com.app.shwe.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.app.shwe.dto.NewsRequestDTO;
import com.app.shwe.dto.SamplePhotoRequest;
import com.app.shwe.dto.SearchDTO;
import com.app.shwe.model.News;
import com.app.shwe.model.SamplePhoto;
import com.app.shwe.service.NewsService;
import com.app.shwe.service.SamplePhotoService;

@RestController
@RequestMapping("/api/v1/sample-photo")
public class SamplePhotoController {
	
	@Autowired
	private SamplePhotoService samplePhotoService;

	@PostMapping("/saveSamplePhoto")
	public ResponseEntity<String> saveNews(@RequestPart("photo")MultipartFile photo,
			@RequestPart("request") SamplePhotoRequest request) {
		return samplePhotoService.saveSamplePhoto(photo, request);

	}

	@GetMapping("/getSamplePhotoById/{id}")
	public ResponseEntity<SamplePhoto> getNewsById(@PathVariable int id) {
		return samplePhotoService.getSamplePhotoById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@GetMapping("/showAllNews")
	public List<SamplePhoto> showCars() {
		return samplePhotoService.getAllSamplePhoto();
	}

	@PutMapping("/updateSamplePhoto/{id}")
	public ResponseEntity<String> updateNews(@PathVariable int id, @RequestPart("photo") MultipartFile photo,
			@RequestPart("request") SamplePhotoRequest request) {
		return samplePhotoService.updateSamplePhoto(id, photo, request);
	}

	@DeleteMapping("/deleteSamplePhoto/{id}")
	public ResponseEntity<?> deleteCar(@PathVariable int id) {
		return samplePhotoService.deleteSamplePhoto(id);
	}

}

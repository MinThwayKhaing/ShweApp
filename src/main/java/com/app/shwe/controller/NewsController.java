package com.app.shwe.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.app.shwe.dto.CarRentRequestDTO;
import com.app.shwe.dto.NewsRequestDTO;
import com.app.shwe.dto.SearchDTO;
import com.app.shwe.model.CarRent;
import com.app.shwe.model.News;
import com.app.shwe.service.NewsService;

@RestController
@RequestMapping("/api/v1/news")
public class NewsController {

	@Autowired
	private NewsService newsService;

	@PostMapping("/saveNews")
	public ResponseEntity<String> saveNews(@RequestPart("images") List<MultipartFile> images,
			@RequestPart("request") NewsRequestDTO request) {
		return newsService.saveNews(images, request);

	}

	@GetMapping("/getNewsById/{id}")
	public ResponseEntity<News> getNewsById(@PathVariable int id) {
		return newsService.getNewsById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@GetMapping("/showAllNews")
	public Page<News> showCars(@RequestBody SearchDTO search) {
		return newsService.getAllNewsByDate(search);
	}

	@PutMapping("/updateNews/{id}")
	public ResponseEntity<String> updateNews(@PathVariable int id, @RequestPart("images") List<MultipartFile> images,
			@RequestPart("request") NewsRequestDTO request) {
		return newsService.updateNews(id, images, request);
	}

	@DeleteMapping("/deleteNews/{id}")
	public ResponseEntity<?> deleteCar(@PathVariable int id) {
		return newsService.deleteNews(id);
	}

}
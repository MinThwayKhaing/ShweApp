package com.app.shwe.service;

import java.io.IOException;
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
import com.app.shwe.model.CarRent;
import com.app.shwe.model.News;
import com.app.shwe.model.Translator;
import com.app.shwe.model.VisaExtensionType;
import com.app.shwe.repository.NewsRepository;
import com.app.shwe.repository.UserRepository;
import com.app.shwe.utils.FilesSerializationUtil;
import com.app.shwe.utils.SecurityUtils;

import jakarta.transaction.Transactional;

@Service
public class NewsService {

	@Autowired
	private NewsRepository newsRepository;

	@Autowired
	private FileUploadService fileUploadService;

	@Autowired
	private UserRepository userRepository;

	@PreAuthorize("hasRole('ADMIN')")
	@Transactional
	public ResponseEntity<?> saveNews(List<MultipartFile> images, NewsRequestDTO request) {
		if (images == null && request == null) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error occurred while saving news: ");
		}
		try {
			List<String> imageUrl = fileUploadService.uploadFiles(images);
			String serializedIages = FilesSerializationUtil.serializeImages(imageUrl);
			News news = new News();
			news.setTitle(request.getTitle());
			news.setImages(serializedIages);
			news.setDate(new Date());
			news.setDescription(request.getDescription());
			news.setCreatedBy(userRepository.authUser(SecurityUtils.getCurrentUsername()));
			news.setCreatedDate(new Date());
			news.setDeleteStatus(false);
			newsRepository.save(news);
			return ResponseEntity.status(HttpStatus.OK).body("News saved successfully.");
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error occurred while saving news: " + e.getMessage());
		}
	}

	@PreAuthorize("hasRole('ADMIN')")
	@Transactional
	public ResponseEntity<?> updateNews(int id, List<MultipartFile> files, NewsRequestDTO request) {
		Optional<News> getNews = newsRepository.findById(id);
		if (!getNews.isPresent()) {
			throw new IllegalArgumentException("ID not found");
		}

		try {
			News news = getNews.get();
			if (files != null && !files.isEmpty()) {
				List<String> imageUrl = fileUploadService.uploadFiles(files);
				String serializedImages = FilesSerializationUtil.serializeImages(imageUrl);
				news.setImages(serializedImages);
			}
			news.setTitle(request.getTitle());
			news.setDescription(request.getDescription());
			news.setDate(new Date());
			news.setUpdatedBy(userRepository.authUser(SecurityUtils.getCurrentUsername()));
			news.setUpdatedDate(new Date());
			newsRepository.save(news);
			return ResponseEntity.status(HttpStatus.OK).body("News updated successfully");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred: " + e.getMessage());
		}

	}

	// @Transactional
	// public Optional<News> getNewsById(Integer id) {
	// if (id == null) {
	// throw new IllegalArgumentException("Id cannot be null");
	// }
	// Optional<News> news = newsRepository.findById(id);
	// return news;
	// }

	@Transactional
	public ResponseEntity<News> getNewsById(int id) {
		Optional<News> visaTypeOpt = newsRepository.findById(id);
		if (!visaTypeOpt.isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Return 404 if not found
		}

		return new ResponseEntity<>(visaTypeOpt.get(), HttpStatus.OK); // Return the found VisaExtensionType
	}

	@PreAuthorize("hasRole('ADMIN')")
	@Transactional
	public ResponseEntity<?> deleteNews(int id) {
		int count = newsRepository.checkNewsById(id);

		if (count == 0) {
			return new ResponseEntity<>("News with ID " + id + " not found", HttpStatus.NOT_FOUND);
		}

		try {
			newsRepository.deleteById(id);
			return new ResponseEntity<>("News deleted successfully", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public Page<News> getAllNewsByDate(int page, int size) {
		Pageable pageable = PageRequest.of(page < 1 ? 0 : page - 1, size);
		return newsRepository.getAllNewsByDate(pageable);
	}

}

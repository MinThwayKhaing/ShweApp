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
import com.app.shwe.repository.NewsRepository;
import com.app.shwe.utils.FilesSerializationUtil;
import com.app.shwe.utils.SecurityUtils;

import jakarta.transaction.Transactional;

@Service
public class NewsService {

	@Autowired
	private NewsRepository newsRepository;

	@Autowired
	private FileUploadService fileUploadService;

	@PreAuthorize("hasRole('ADMIN')")
	@Transactional
	public ResponseEntity<String> saveNews(List<MultipartFile> files, String description) {
		if (files == null && description == null) {
			throw new IllegalArgumentException("Request and required fields must not be null");
		}
		try {
			List<String> imageUrl = fileUploadService.uploadFiles(files);
			String serializedIages = FilesSerializationUtil.serializeImages(imageUrl);
			News news = new News();
			news.setImages(serializedIages);
			news.setDate(new Date());
			news.setDescription(description);
			news.setCreatedBy(SecurityUtils.getCurrentUsername());
			news.setCreatedDate(new Date());
			newsRepository.save(news);
			return ResponseEntity.status(HttpStatus.OK).body("News saved successfully.");
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error occurred while saving news: " + e.getMessage());
		}
	}

	@PreAuthorize("hasRole('ADMIN')")
	@Transactional
	public ResponseEntity<String> updateNews(int id, List<MultipartFile> files,String description) {
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
			news.setDescription(description);
			news.setDate(new Date());
			news.setUpdatedBy(SecurityUtils.getCurrentUsername());
			news.setUpdatedDate(new Date());
			newsRepository.save(news);
			return ResponseEntity.status(HttpStatus.OK).body("News updated successfully.");
		} catch (Exception e) {
			return new ResponseEntity<>("Error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@Transactional
	public Optional<News> getNewsById(Integer id) {
		if (id == null) {
			throw new IllegalArgumentException("Id cannot be null");
		}
		Optional<News> news = newsRepository.findById(id);
		return news;
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

	public Page<News> getAllNewsByDate(SearchDTO search) {
		int page = (search.getPage() < 1) ? 0 : search.getPage() - 1;
		int size = search.getSize();
		Pageable pageable = PageRequest.of(page, size);
		return newsRepository.getAllNewsByDate(pageable);
	}
}

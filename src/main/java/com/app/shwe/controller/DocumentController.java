package com.app.shwe.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.app.shwe.service.DocumentsService;

@RestController
@RequestMapping("/api/v1/documents")
public class DocumentController {
	
	@Autowired
	private DocumentsService documentsService;

	@PostMapping("/saveDocuments")
	public ResponseEntity<String> saveNews(@RequestPart("images") List<MultipartFile> images) {
		return documentsService.saveDocuments(images);
	}

}

package com.app.shwe.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.app.shwe.dto.DocumentDTO;
import com.app.shwe.dto.NewsRequestDTO;
import com.app.shwe.model.Documents;
import com.app.shwe.model.MainOrder;
import com.app.shwe.model.News;
import com.app.shwe.model.User;
import com.app.shwe.repository.DocumentsRepository;
import com.app.shwe.repository.MainOrderRepository;
import com.app.shwe.repository.NewsRepository;
import com.app.shwe.repository.UserRepository;
import com.app.shwe.utils.FilesSerializationUtil;
import com.app.shwe.utils.SecurityUtils;

import jakarta.transaction.Transactional;

@Service
public class DocumentsService {
	
	@Autowired
	private DocumentsRepository documentsRepository;

	@Autowired
	private FileUploadService fileUploadService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private MainOrderRepository mainOrderRepository;
	
	@Transactional
    public ResponseEntity<String> saveDocuments(String sysKey,List<MultipartFile> images) {
        if (images == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while saving documents: images are null");
        }
        try {
            Optional<MainOrder> mainOrderOptional = mainOrderRepository.findBySysKey(sysKey);
            if (!mainOrderOptional.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Main order not found for sysKey: " + sysKey);
            }
            MainOrder mainOrder = mainOrderOptional.get();
            
            List<String> imageUrl = fileUploadService.uploadFiles(images);
            String serializedImages = FilesSerializationUtil.serializeImages(imageUrl);
            
            Documents documents = new Documents();
            documents.setImages(serializedImages);
            documents.setCreatedBy(userRepository.authUser(SecurityUtils.getCurrentUsername()));
            documents.setCreatedDate(new Date());
            documents.setOrder(mainOrder);
            documents.setDeleteStatus(false);
            
            documentsRepository.save(documents);
            return ResponseEntity.status(HttpStatus.OK).body("Documents saved successfully.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while saving documents: " + e.getMessage());
        }
    }
	
	public ResponseEntity<List<DocumentDTO>> getDocumentsBySyskey(String syskey) {
	    Optional<MainOrder> mainOrderOptional = mainOrderRepository.findBySysKey(syskey);
	    if (!mainOrderOptional.isPresent()) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                .body(null);
	    }
	    MainOrder mainOrder = mainOrderOptional.get();
	    List<Documents> documentsList = documentsRepository.findByOrder(mainOrder.getSys_key());
	    
	    // Convert Documents to DocumentDTO
	    List<DocumentDTO> documentDTOs = documentsList.stream()
	        .map(doc -> new DocumentDTO(doc.getImages()))
	        .collect(Collectors.toList());
	    
	    return ResponseEntity.status(HttpStatus.OK).body(documentDTOs);
	}

}



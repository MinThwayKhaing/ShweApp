package com.app.shwe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.app.shwe.dto.Report90DayRequestDTO;
import com.app.shwe.model.User;
import com.app.shwe.repository.UserRepository;
import com.app.shwe.utils.SecurityUtils;

@Service
public class Report90DayService {
	
	@Autowired
	private UserRepository userRepo;
	
	public ResponseEntity<String> saveReport90Day(MultipartFile tm6Photo, MultipartFile expireDatePhoto,
			MultipartFile passportBio, MultipartFile visaPage,Report90DayRequestDTO request){
		if (tm6Photo == null && expireDatePhoto == null && passportBio == null &&  visaPage == null &&  request == null) {
			throw new IllegalArgumentException("Request and required fields must not be null");
		}
		try {
			int userId = userRepo.authUser(SecurityUtils.getCurrentUsername());
			User user = userRepo.findById(userId)
					.orElseThrow(() -> new RuntimeException("User not found for ID: " + userId));
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
		
	}

}

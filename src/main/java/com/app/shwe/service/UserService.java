package com.app.shwe.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.shwe.datamapping.TranslatorOrderMapping;
import com.app.shwe.dto.ChangePasswordDTO;
import com.app.shwe.dto.TranslatorRequestDTO;
import com.app.shwe.dto.UserReportDTO;
import com.app.shwe.model.Report;
import com.app.shwe.model.TranslatorOrder;
import com.app.shwe.model.User;
import com.app.shwe.repository.TranslatorOrderRepostitory;
import com.app.shwe.repository.TranslatorRepository;
import com.app.shwe.repository.UserRepository;
import com.app.shwe.utils.SecurityUtils;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public List<UserReportDTO> getReportByUserId(String id) {
		List<UserReportDTO> dtoList = userRepo.findUserContentById(id);

		List<UserReportDTO> result = new ArrayList<>();

		for (UserReportDTO dto : dtoList) {
			UserReportDTO userReportDTO = new UserReportDTO();
			User user = new User();
			user.setUserName(dto.getUsername());
			userReportDTO.setUsername(dto.getUsername());
			Report report = new Report();
			report.setContent(dto.getReport());
			userReportDTO.setReport(dto.getReport());
			result.add(userReportDTO);
		}

		return result;
	}
	
	public ResponseEntity<String> changePassword(ChangePasswordDTO dto){
		if(dto == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Request data is null.");
		}
		try {
			if(dto.getPassword().equals(dto.getConfrimPasswrod())) {
				int userId = userRepo.authUser(SecurityUtils.getCurrentUsername());
				String encodePassword = passwordEncoder.encode(dto.getConfrimPasswrod());
				userRepo.changePassword(userId, encodePassword);
				return ResponseEntity.status(HttpStatus.OK).body("Password change successfully.");
			}else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password didn't match with confirm password.");
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error occurred while saving translator: " + e.getMessage());
		}
	}
	
	

}

package com.app.shwe.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.app.shwe.dto.ChangePasswordDTO;
import com.app.shwe.dto.UserReportDTO;
import com.app.shwe.model.Report;
import com.app.shwe.model.User;
import com.app.shwe.repository.UserRepository;
import com.app.shwe.utils.SecurityUtils;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private FileUploadService fileUploadService;

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

	public ResponseEntity<String> changePassword(ChangePasswordDTO dto) {
		if (dto == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Request data is null.");
		}
		try {
			int userId = userRepo.authUser(SecurityUtils.getCurrentUsername());
			User user = userRepo.findById((int) userId).orElse(null);

			if (user == null) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
			}

			if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Password didn't match");
			}

			String encodePassword = passwordEncoder.encode(dto.getNewPasswrod());
			userRepo.changePassword(userId, encodePassword);

			return ResponseEntity.status(HttpStatus.OK).body("Password changed successfully.");

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error occurred while changing password: " + e.getMessage());
		}
	}

	public ResponseEntity<String> updateImages(MultipartFile images) {

		try {
			if (images == null) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.body("Error occurred while saving news: ");
			}

			int userId = userRepo.authUser(SecurityUtils.getCurrentUsername());

			String recentImage = userRepo.selectImage(userId);
            if( !recentImage.equals("")){
				boolean status = fileUploadService.deleteFile(recentImage);
				if (!status) {
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
							.body("Error occured when deleting s3 link");
				}
			}
			String imageUrl = fileUploadService.uploadFile(images);
            System.out.println("This is image url" + imageUrl);
			userRepo.imageUpdate(userId, imageUrl);
			return ResponseEntity.status(HttpStatus.OK).body("Image Updaet  successfully.");

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error occurred while changing password: " + e.getMessage());
		}

	}

}

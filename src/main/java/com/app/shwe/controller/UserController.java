package com.app.shwe.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.app.shwe.dto.ChangePasswordDTO;
import com.app.shwe.dto.TranslatorRequestDTO;
import com.app.shwe.dto.UserReportDTO;
import com.app.shwe.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping("/getUserReport/{id}")
	public List<UserReportDTO> getReportByUserId(@PathVariable String id) {
		List<UserReportDTO> dto = userService.getReportByUserId(id);
		return dto;
	}

	@PostMapping("/changePassword")
	public ResponseEntity<String> changePassword(@RequestBody ChangePasswordDTO dto) {
		return userService.changePassword(dto);
	}

	@PostMapping("user-update-image")
	public ResponseEntity<String> updateImage(@RequestPart("image") MultipartFile image) {

		return userService.updateImages(image);
	}

}

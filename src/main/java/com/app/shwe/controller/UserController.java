package com.app.shwe.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	public ResponseEntity<List<UserReportDTO>> getReportByUserId(@PathVariable String id) {
		List<UserReportDTO> dto = userService.getReportByUserId(id);
		return ResponseEntity.ok(dto);
	}

}

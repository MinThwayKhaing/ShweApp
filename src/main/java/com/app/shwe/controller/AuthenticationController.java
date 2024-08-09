package com.app.shwe.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.app.shwe.dto.AuthenticationRequest;
import com.app.shwe.dto.AuthenticationResponse;
import com.app.shwe.dto.RegisterRequest;
import com.app.shwe.dto.UserRequest;
import com.app.shwe.model.Role;
import com.app.shwe.service.AuthenticationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

	@Autowired
	private AuthenticationService authenticationService;

	@PostMapping("/register-user")
	public ResponseEntity<String> registerUser(
			@RequestBody RegisterRequest request) {
		return authenticationService.registerUser(request);
	}

	@PostMapping("/register-admin")
	public ResponseEntity<String> registerAdmin(
			@RequestBody RegisterRequest request) {
		return authenticationService.registerAdmin(request);
	}

	@PostMapping("/login")
	public ResponseEntity<?> Login(@RequestBody AuthenticationRequest request) {
		return authenticationService.login(request);
	}

	@PostMapping("/refresh-token")
	public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> request) {
		String refreshToken = request.get("refreshToken");
		ResponseEntity<?> response = authenticationService.refreshToken(refreshToken);
		return response;
	}

}

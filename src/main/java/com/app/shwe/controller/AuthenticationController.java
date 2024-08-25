package com.app.shwe.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.app.shwe.dto.AuthenticationRequest;
import com.app.shwe.dto.AuthenticationResponse;
import com.app.shwe.dto.OtpVerificationRequest;
import com.app.shwe.dto.RegisterRequest;
import com.app.shwe.dto.UpdateUserRequest;
import com.app.shwe.dto.UserRequest;
import com.app.shwe.model.Role;
import com.app.shwe.service.AuthenticationService;
import com.app.shwe.service.OtpService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
	@Autowired
	private OtpService smsOtpService;
	@Autowired
	private AuthenticationService authenticationService;

	@PostMapping("/initiate-register-user")
	public ResponseEntity<?> registerInitiateUser(@RequestBody RegisterRequest request) {
		return authenticationService.initiateRegistration(request);
	}

	@PostMapping("/register-admin")
	public ResponseEntity<String> registerAdmin(@RequestBody RegisterRequest request) {
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

	@PostMapping("/verify")
	public ResponseEntity<String> verifyOtp(@RequestBody OtpVerificationRequest request) {
		try {
			return smsOtpService.verifyOtp(request.getToken(), request.getOtpCode(), request.getRecipient());
		} catch (Exception e) {
			return ResponseEntity.status(500).build(); // HTTP 500 Internal Server Error
		}
	}

	@PostMapping("/update-user")
	public ResponseEntity<String> updateUser(@RequestBody UpdateUserRequest request) {
		return authenticationService.initiateUpdateUser(request);
	}

	@PostMapping("/verify-update-otp")
	public ResponseEntity<String> verifyUpdateOtp(@RequestBody OtpVerificationRequest otpRequest) {
		return smsOtpService.verifyOtpForUpdate(otpRequest.getToken(), otpRequest.getOtpCode(), otpRequest.getRecipient());
	}

}

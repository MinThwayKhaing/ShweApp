package com.app.shwe.service;

import java.util.Date;
import java.util.Optional;

import org.apache.catalina.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.app.shwe.dto.AuthenticationRequest;
import com.app.shwe.dto.AuthenticationResponse;
import com.app.shwe.dto.RegisterRequest;
import com.app.shwe.dto.UpdateUserRequest;
import com.app.shwe.dto.UserRequest;
import com.app.shwe.model.PendingRegistration;
import com.app.shwe.model.Role;
import com.app.shwe.model.User;
import com.app.shwe.repository.PendingRegistrationRepository;
import com.app.shwe.repository.UserRepository;
import com.app.shwe.securityConfig.JwtService;
import com.app.shwe.utils.SecurityUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

	@Autowired
	private UserRepository repository;
	private final PasswordEncoder passwordEncoder;
	@Autowired
	private JwtService jwtService;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private FileUploadService fileUploadService;
	@Autowired
	@Lazy
	private OtpService otpService;
	@Autowired
	private PendingRegistrationRepository pendingRegistrationRepo;

	public ResponseEntity<String> initiateRegistration(RegisterRequest request) {
		if (repository.existsByPhoneNumber(request.getPhoneNumber())) {
			throw new RuntimeException("Phone number already registered.");
		}
		String otpCode = otpService.generateOtp(6);
		Date expiryTime = otpService.calculateExpiryTime(3);

		PendingRegistration pendingRegistration = new PendingRegistration();
		pendingRegistration.setName(request.getUserName());
		pendingRegistration.setPhoneNumber(request.getPhoneNumber());
		pendingRegistration.setPasswordHash(request.getPassword());
		pendingRegistration.setOtp(otpCode);
		pendingRegistration.setOtpExpiry(expiryTime);

		pendingRegistrationRepo.save(pendingRegistration);

		String token = otpService.sendOtp(request.getPhoneNumber(), "SHWEAPPS", otpCode);
		return ResponseEntity.status(HttpStatus.OK).body(token);
	}

	// WWTT
	public ResponseEntity<String> initiateUpdateUser(UpdateUserRequest request) {

		int id = repository.authUser(SecurityUtils.getCurrentUsername());
		Optional<User> existingUser = repository.getUserById(id);

		try {
			if (!existingUser.isPresent()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
			} else {
				User user = existingUser.get();
				user.setUserName(request.getUserName());
				repository.save(user);
				return new ResponseEntity<>("User Name updated successfully", HttpStatus.OK);
			}

		} catch (Exception e) {
			return new ResponseEntity<>("Error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	public ResponseEntity<String> registerUser(String phoneNumber) {
		if (repository.existsByPhoneNumber(phoneNumber) || repository.existsByUserName(phoneNumber)) {
			throw new IllegalArgumentException("User with the same phone number or username already exists");
		}
		Optional<PendingRegistration> pendingUserOptional = pendingRegistrationRepo.findByPhoneNumber(phoneNumber);

		// Check if pendingUserOptional contains a value
		if (!pendingUserOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error occurred while saving User: Pending registration not found.");
		}

		PendingRegistration pendingUser = pendingUserOptional.get();

		try {
			var user = User.builder().phoneNumber(pendingUser.getPhoneNumber()).userName(pendingUser.getName())
					.password(passwordEncoder.encode(pendingUser.getPasswordHash())).image("").role(Role.USER).build();

			repository.save(user);
			pendingRegistrationRepo.delete(pendingUser);
			return ResponseEntity.status(HttpStatus.OK).body("User saved successfully.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error occurred while saving users: " + e.getMessage());
		}
	}

	// WWTT
	public ResponseEntity<String> updateUserPhoneNumber(String phoneNumber) {
		Optional<PendingRegistration> pendingUserOptional = pendingRegistrationRepo.findByPhoneNumber(phoneNumber);

		if (!pendingUserOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Pending registration not found.");
		}

		PendingRegistration pendingUser = pendingUserOptional.get();
		int id = repository.authUser(SecurityUtils.getCurrentUsername());
		Optional<User> existingUser = repository.getPhoneNumberById(id);

		if (!existingUser.isPresent()) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("User not found.");
		}

		try {
			User user = existingUser.get();
			user.setPhoneNumber(pendingUser.getPhoneNumber());
			user.setUserName(pendingUser.getName());
			repository.save(user);
			pendingRegistrationRepo.delete(pendingUser);

			return ResponseEntity.status(HttpStatus.OK).body("User updated successfully.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error occurred while updating user: " + e.getMessage());
		}
	}

	public ResponseEntity<String> registerAdmin(RegisterRequest request) {
		if (request == null) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while saving news: ");
		}

		// String imageUrl = fileUploadService.uploadFile(image);
		try {
			var user = User.builder().phoneNumber(request.getPhoneNumber()).userName(request.getUserName())
					.password(passwordEncoder.encode(request.getPassword())).image("").role(Role.ADMIN).build();

			if (repository.existsByPhoneNumber(request.getPhoneNumber())
					|| repository.existsByUserName(request.getPhoneNumber())) {
				throw new IllegalArgumentException("User with the same phone number or username already exists");
			}

			repository.save(user);
			return ResponseEntity.status(HttpStatus.OK).body("User saved successfully.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error occurred while saving users: " + e.getMessage());
		}

	}

	public ResponseEntity<?> login(AuthenticationRequest request) {
		if (request == null || request.getPhoneNumber() == null || request.getPassword() == null) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("request body must not be null ");
		}
		try {
			User authenticatedUser = repository.findByPhoneNumber(request.getPhoneNumber());

			if (authenticatedUser == null
					|| !passwordEncoder.matches(request.getPassword(), authenticatedUser.getPassword())) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found or invalid credentials");
			}

			UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
					authenticatedUser.getUsername(), request.getPassword());

			Authentication authentication = authenticationManager.authenticate(token);
			SecurityContextHolder.getContext().setAuthentication(authentication);

			String jwtToken = jwtService.generateToken(authenticatedUser);
			String refreshToken = jwtService.generateRefreshToken(authenticatedUser.getPhoneNumber());

			AuthenticationResponse response = AuthenticationResponse.builder().token(jwtToken)
					.refreshToken(refreshToken).user(authenticatedUser).build();

			return ResponseEntity.ok(response);
		} catch (Exception e) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error occurred while login " + e.getMessage());
		}
	}

	public ResponseEntity<?> refreshToken(String refreshToken) {
		if (refreshToken == null) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("request body must not be null");
		}

		if (jwtService.isRefreshTokenValid(refreshToken)) {
			String phoneNumber = jwtService.extractUsernameFromRefreshToken(refreshToken);

			if (phoneNumber == null) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
						.body("Phone number extracted from refresh token is null");
			}

			User user = repository.findByPhoneNumber(phoneNumber);

			if (user == null) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found or invalid credentials");
			}

			String newAccessToken = jwtService.generateToken(user);
			String newRefreshToken = jwtService.generateRefreshToken(user.getPhoneNumber());

			AuthenticationResponse response = AuthenticationResponse.builder().token(newAccessToken)
					.refreshToken(newRefreshToken).user(user).build();

			return ResponseEntity.ok(response);
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
		}
	}

}

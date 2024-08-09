package com.app.shwe.service;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.app.shwe.dto.UserRequest;
import com.app.shwe.model.Role;
import com.app.shwe.model.User;
import com.app.shwe.repository.UserRepository;
import com.app.shwe.securityConfig.JwtService;

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

    public ResponseEntity<String> registerUser(RegisterRequest request) {
        if (request == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while saving news: ");
        }

        // String imageUrl = fileUploadService.uploadFile(image);
        try {
            var user = User.builder()
                    .phoneNumber(request.getPhoneNumber())
                    .userName(request.getUserName())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .image("")
                    .role(Role.USER)
                    .build();

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

    public ResponseEntity<String> registerAdmin(RegisterRequest request) {
        if (request == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while saving news: ");
        }

        // String imageUrl = fileUploadService.uploadFile(image);
        try {
            var user = User.builder()
                    .phoneNumber(request.getPhoneNumber())
                    .userName(request.getUserName())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .image("")
                    .role(Role.ADMIN)
                    .build();

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
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("request body must not be null ");
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

            AuthenticationResponse response = AuthenticationResponse.builder()
                    .token(jwtToken)
                    .refreshToken(refreshToken)
                    .user(authenticatedUser)
                    .build();

            return ResponseEntity.ok(response);
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while login " + e.getMessage());
        }
    }

    public ResponseEntity<?> refreshToken(String refreshToken) {
        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("request body must not be null");
        }

        if (jwtService.isRefreshTokenValid(refreshToken)) {
            String phoneNumber = jwtService.extractUsernameFromRefreshToken(refreshToken);

            if (phoneNumber == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                        "Phone number extracted from refresh token is null");
            }

            User user = repository.findByPhoneNumber(phoneNumber);

            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found or invalid credentials");
            }

            String newAccessToken = jwtService.generateToken(user);
            String newRefreshToken = jwtService.generateRefreshToken(user.getPhoneNumber());

            AuthenticationResponse response = AuthenticationResponse.builder()
                    .token(newAccessToken)
                    .refreshToken(newRefreshToken)
                    .user(user)
                    .build();

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
        }
    }
}

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

    public void register(MultipartFile image, RegisterRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request and required fields must not be null");
        }

        String imageUrl = fileUploadService.uploadFile(image);

        var user = User.builder()
                .phoneNumber(request.getPhoneNumber())
                .userName(request.getUserName())
                .password(passwordEncoder.encode(request.getPassword()))
                .image(imageUrl)
                .role(request.getRole())
                .build();

        if (repository.existsByPhoneNumber(request.getPhoneNumber())
                || repository.existsByUserName(request.getPhoneNumber())) {
            throw new IllegalArgumentException("User with the same phone number or username already exists");
        }

        repository.save(user);
    }

    public ResponseEntity<AuthenticationResponse> login(AuthenticationRequest request) {
        if (request == null || request.getPhoneNumber() == null || request.getPassword() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request and required fields must not be null");
        }

        User authenticatedUser = repository.findByPhoneNumber(request.getPhoneNumber());

        if (authenticatedUser == null
                || !passwordEncoder.matches(request.getPassword(), authenticatedUser.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found or invalid credentials");
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
    }

    public ResponseEntity<AuthenticationResponse> refreshToken(String refreshToken) {
        if (refreshToken == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Refresh token must not be null");
        }

        if (jwtService.isRefreshTokenValid(refreshToken)) {
            String phoneNumber = jwtService.extractUsernameFromRefreshToken(refreshToken);

            if (phoneNumber == null) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                        "Phone number extracted from refresh token is null");
            }

            User user = repository.findByPhoneNumber(phoneNumber);

            if (user == null) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found or invalid credentials");
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
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token");
        }
    }
}

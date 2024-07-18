package com.app.shwe.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.app.shwe.dto.AuthenticationRequest;
import com.app.shwe.dto.AuthenticationResponse;
import com.app.shwe.dto.RegisterRequest;
import com.app.shwe.model.Role;
import com.app.shwe.model.User;
import com.app.shwe.securityConfig.JwtService;
import com.app.shwe.userRepository.UserRepository;

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

    public void register(MultipartFile image,String userName,String phoneNumber,String password,Role role) {
        if ( userName == null || phoneNumber == null || 
        		password == null || role == null) {
            throw new IllegalArgumentException("Request and required fields must not be null");
        }
        
        byte[] imageBytes = null;
        try {
            imageBytes = image.getBytes();
        } catch (IOException e) {
            throw new RuntimeException("Failed to read image file", e);
        }
        
        var user = User.builder()
                .phoneNumber(phoneNumber)
                .userName(userName)
                .password(passwordEncoder.encode(password))
                .image(imageBytes)
                .role(role)
                .build();
        
        if (repository.existsByPhoneNumber(phoneNumber) || 
            repository.existsByUserName(userName)) {
            throw new IllegalArgumentException("User with the same phone number or username already exists");
        }

        repository.save(user);
    }

    public AuthenticationResponse login(AuthenticationRequest request) {
        if (request == null || request.getPhoneNumber() == null || request.getPassword() == null) {
            throw new IllegalArgumentException("Request and required fields must not be null");
        }
        // Find the user by phone number
        User authenticatedUser = repository.findByPhoneNumber(request.getPhoneNumber())
                .orElseThrow(() -> new RuntimeException("User not found or invalid credentials"));
        System.out.println("authenticatedUser"+authenticatedUser);
        // Verify the password
        if (!passwordEncoder.matches(request.getPassword(), authenticatedUser.getPassword())) {
            throw new RuntimeException("User not found or invalid credentials");
        }
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(authenticatedUser.getUsername(), request.getPassword());

        Authentication authentication = authenticationManager.authenticate(token);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwtToken = jwtService.generateToken(authenticatedUser);
        String refreshToken = jwtService.generateRefreshToken(authenticatedUser.getPhoneNumber());

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .refreshToken(refreshToken)
                .user(authenticatedUser)
                .build();
    }

    public AuthenticationResponse refreshToken(String refreshToken) {
        if (refreshToken == null) {
            throw new IllegalArgumentException("Refresh token must not be null");
        }

        if (jwtService.isRefreshTokenValid(refreshToken)) {
            String phoneNumber = jwtService.extractUsernameFromRefreshToken(refreshToken);
            
            if (phoneNumber == null) {
                throw new RuntimeException("Phone number extracted from refresh token is null");
            }

            User user = repository.findByPhoneNumber(phoneNumber)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            String newAccessToken = jwtService.generateToken(user);
            String newRefreshToken = jwtService.generateRefreshToken(user.getPhoneNumber());

            return AuthenticationResponse.builder()
                    .token(newAccessToken)
                    .refreshToken(newRefreshToken)
                    .user(user)
                    .build();
        } else {
            throw new RuntimeException("Invalid refresh token");
        }
    }
}

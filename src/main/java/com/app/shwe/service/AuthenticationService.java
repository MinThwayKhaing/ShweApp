package com.app.shwe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.shwe.dto.AuthenticationRequest;
import com.app.shwe.dto.AuthenticationResponse;
import com.app.shwe.dto.RegisterRequest;
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

    public void register(RegisterRequest request) {
        if (request == null || request.getPhoneNumber() == null || request.getUserName() == null || 
            request.getPassword() == null || request.getRole() == null) {
            throw new IllegalArgumentException("Request and required fields must not be null");
        }
        
        var user = User.builder()
                .phoneNumber(request.getPhoneNumber())
                .userName(request.getUserName())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();
        
        if (repository.existsByPhoneNumber(request.getPhoneNumber()) || 
            repository.existsByUserName(request.getUserName())) {
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

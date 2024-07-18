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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.app.shwe.dto.AuthenticationRequest;
import com.app.shwe.dto.AuthenticationResponse;
import com.app.shwe.model.Role;
import com.app.shwe.service.AuthenticationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
	
	    @Autowired
	    private AuthenticationService authenticationService;

	    @PostMapping("/register")
	    public ResponseEntity<Map<String, String>> register(@RequestParam("image")MultipartFile image,
	    		@RequestParam("userName") String userName,@RequestParam("phoneNumber")String phoneNumber
	    		 ,@RequestParam("password") String password,@RequestParam("role") Role role) {
	        try {
	            authenticationService.register(image,userName,phoneNumber,password,role);
	            Map<String, String> response = new HashMap<>();
	            response.put("message", "Registration successful");
	            return ResponseEntity.ok(response);
	        } catch (RuntimeException e) {
	            Map<String, String> response = new HashMap<>();
	            response.put("message", e.getMessage());
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	        }
	    }

	    
	    @PostMapping("/login")
	    public ResponseEntity<AuthenticationResponse> Login(@RequestBody AuthenticationRequest request){
	        return ResponseEntity.ok(authenticationService.login(request));
	    }
	    
	    
	    @PostMapping("/refresh-token")
	    public ResponseEntity<AuthenticationResponse> refreshToken(@RequestBody Map<String, String> request) {
	        String refreshToken = request.get("refreshToken");
	        AuthenticationResponse response = authenticationService.refreshToken(refreshToken);
	        return ResponseEntity.ok(response);
	    }

}

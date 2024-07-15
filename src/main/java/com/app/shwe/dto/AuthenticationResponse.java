package com.app.shwe.dto;

import org.springframework.security.core.Authentication;

import com.app.shwe.model.Role;
import com.app.shwe.model.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
	
	private String token;
	 private String refreshToken;
	 private User user;
}

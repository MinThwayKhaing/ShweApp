package com.app.shwe.dto;

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
	 private UserResponseDTO user;
}

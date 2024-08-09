package com.app.shwe.dto;

import org.springframework.web.multipart.MultipartFile;

import com.app.shwe.model.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

	private String phoneNumber;
	private String password;
	private String userName;
	// private Role role;
}

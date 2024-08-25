package com.app.shwe.dto;

import com.app.shwe.model.CarPrice;
import com.app.shwe.model.CarRent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequest {
	
	private int userId;
    private String userName;
    private String phoneNumber;

}

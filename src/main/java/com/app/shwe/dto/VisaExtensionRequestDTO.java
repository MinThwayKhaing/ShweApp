package com.app.shwe.dto;

import com.app.shwe.model.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VisaExtensionRequestDTO {

	private String contactNumber;
	private int visa_id;

}

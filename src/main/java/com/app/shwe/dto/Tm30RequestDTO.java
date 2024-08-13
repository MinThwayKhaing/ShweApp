package com.app.shwe.dto;

import java.util.List;

import jakarta.persistence.ElementCollection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Tm30RequestDTO {
	
	private String period;
	private String passportBio;
	private String visaPage;
	private String contactNumber;
	private String duration;
	private int visa_id;

}

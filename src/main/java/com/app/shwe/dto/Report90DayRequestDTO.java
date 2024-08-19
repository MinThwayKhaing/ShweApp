package com.app.shwe.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Report90DayRequestDTO {
	
	private String visaType;
	private String contactNumber;
	private int visa_id; 

}

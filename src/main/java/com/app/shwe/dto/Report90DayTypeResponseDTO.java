package com.app.shwe.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Report90DayTypeResponseDTO {
	
	private int order_id;
	private String visa_service;
	private int main_visa_id;
	private int sub_visa_id;
	private String description;
	private double price;

}

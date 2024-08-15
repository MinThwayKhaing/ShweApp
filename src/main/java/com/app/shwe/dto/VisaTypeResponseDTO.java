package com.app.shwe.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VisaTypeResponseDTO {
    
	private int id;
	
    private String visaType;
    
    private double price;

    private String duration;

}

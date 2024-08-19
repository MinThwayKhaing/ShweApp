package com.app.shwe.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Report90DayTypeRequestDTO {
	
private String description;
    
    private double price;

    private int subVisaId;
    
    private int visa_service_id;

}

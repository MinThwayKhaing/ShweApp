package com.app.shwe.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VisaTypeRequestDTO {
    
    private String description;
    
    private double price;

    private String duration;
    
    private int subVisaId;
    
    private int visa_service_id;
}

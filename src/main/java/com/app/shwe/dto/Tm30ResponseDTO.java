package com.app.shwe.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Tm30ResponseDTO {

	private Tm30ProjectionDTO tm30Order;
	
	private List<VisaResponseDTO> visaOrder;


}
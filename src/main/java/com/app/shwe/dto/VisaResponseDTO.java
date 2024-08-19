package com.app.shwe.dto;

import java.io.Serializable;

import com.app.shwe.model.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VisaResponseDTO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int order_id;
	private String visa_service;
	private int main_visa_id;
	private int sub_visa_id;
	private String description;
	private double price;
	private String duration;

}

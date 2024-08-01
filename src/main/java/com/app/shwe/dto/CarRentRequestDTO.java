package com.app.shwe.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CarRentRequestDTO {
	
	private int id;
	private String carName;
	private String ownerName;
	private String carNo;
	private boolean status;
	private String license;
	private String review;
	private String driverName;
	private String driverPhoneNumber;
	private String carColor;
	private int carType;
	private double insideTownPrice;
	private double outsideTownPrice;
	private boolean withDriver;
	private String checkStatus;


}

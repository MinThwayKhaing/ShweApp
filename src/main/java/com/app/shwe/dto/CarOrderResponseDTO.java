package com.app.shwe.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CarOrderResponseDTO {

	private int orderId;
	private int car_id;
	private Date createdDate;
	private int carType;
	private String carBrand;
	private String driverName;
	private String image;
	private String status;
}

package com.app.shwe.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

import com.app.shwe.model.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CarOrderRequestDTO {

	private int order_id;
    private String fromLocation;
    private String toLocation;
    private LocalDate pickUpDate;
    private LocalTime pickUpTime;
    private Date fromDate;
    private Date toDate;
    private int carType;
    private boolean driver;
    // with driver is true;without driver is false;
    private String status;
    private String customerPhoneNumber;
    private String pickUpLocation;
    private String carBrand;
    private int carId;
}

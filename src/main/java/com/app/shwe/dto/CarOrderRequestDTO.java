package com.app.shwe.dto;

import java.io.Serializable;
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
public class CarOrderRequestDTO implements Serializable {

    private int order_id;
    private String fromLocation;
    private int toLocation;
    private LocalDate pickUpDate;
    private LocalTime pickUpTime;
    private Date fromDate;
    private Date toDate;
    private int carType;
    private boolean driver;
    private String sys_key;
    private String status;
    private String customerPhoneNumber;
    private String pickUpLocation;
    private String carBrand;
    private int carId;
    private double price;
}

package com.app.shwe.dto;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CarOrderResponseDTO {

    private Integer userId;
    private String userName;
    private String fromLocation;
    private String toLocation;
    private LocalDate pickUpDate;
    private LocalTime pickUpTime;
    private Date fromDate;
    private Date toDate;
    private Integer carType;
    private boolean driver;
    private boolean orderConfirm;
    private String customerPhoneNumber;
    private String carBrand;
    private Integer carId;
}

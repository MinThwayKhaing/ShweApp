package com.app.shwe.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarOrderResponseAdminDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String sysKey;
    private String fromLocation;
    private Integer toLocation; // Changed to Integer
    private LocalDate pickUpDate;
    private LocalTime pickUpTime;
    private Date fromDate;
    private Date toDate;
    private Integer carType; // Changed to Integer
    private Boolean driver; // Changed to Boolean
    private String status;
    private String customerPhoneNumber;
    private String pickUpLocation;
    private String carBrand;
    private Double price; // Changed to Double
    private Date createdDate;

    // Fields from CarRent
    private Integer id; // Changed to Integer
    private String carName;
    private String ownerName;
    private String carNo;
    private Boolean carStatus; // Changed to Boolean
    private String license;
    private String carImage;
    private String driverName;
    private String driverPhoneNumber;
    private String carColor;

    // Fields from User
    private Long userId; // Changed to Long
    private String userName;
    private String userImage;

}

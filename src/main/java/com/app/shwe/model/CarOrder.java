package com.app.shwe.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarOrder extends CommonDTO {

	private String fromLocation;
	private String toLocation;
	private LocalDate pickUpDate;
	private LocalTime pickUpTime;
	private Date fromDate;
	private Date toDate;
	private int carType;
	private boolean driver;
	private String status;
	private String customerPhoneNumber;
	private String carBrand;
	@ManyToOne
	@JoinColumn(name = "car_id")
	private CarRent carId;

}

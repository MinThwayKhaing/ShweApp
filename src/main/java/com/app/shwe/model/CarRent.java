package com.app.shwe.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarRent extends CommonDTO {

	private String carName;
	private String ownerName;
	private String carNo;
	private boolean status;
	private String license;
	private String review;
	private String driverName;
	private String carColor;
	private int carType;
	@OneToOne(mappedBy = "car", cascade = CascadeType.ALL, orphanRemoval = true)
	private CarPrice carPrice;

}

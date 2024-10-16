package com.app.shwe.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
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
	@Lob
	@Column(name = "image")
	private String image;
	// private String review;
	private String driverName;
	private String driverPhoneNumber;
	private String carColor;
	private int carType;

}

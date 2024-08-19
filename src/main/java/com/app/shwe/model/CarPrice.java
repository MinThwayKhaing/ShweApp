package com.app.shwe.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarPrice extends CommonDTO {

	private double price;
	private int type;

	@ManyToOne
	@JoinColumn(name = "location_id")
	private CarRentLocation carRentLocation;
}

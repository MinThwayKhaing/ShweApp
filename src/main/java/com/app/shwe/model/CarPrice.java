package com.app.shwe.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarPrice extends CommonDTO {

	private double insideTownPrice;
	private double outsideTownPrice;
	// private boolean withDriver;

	@OneToOne
	@JoinColumn(name = "car_id")
	private CarRent car;

}

package com.app.shwe.model;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarOrder extends CommonDTO {

	private int carType;
	private Date fromDate;
	private Date toDate;
	private boolean driver;
	// with driver is true;without driver is false;
	private boolean travelrange;
	// inside is false;outside is true
	private boolean orderConfirm;
	// private Long user_id;
	private int car;
}

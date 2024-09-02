package com.app.shwe.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "report90_day_visa_type")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Report90DayVisaType extends CommonDTO {

	@Column(name = "description")
	private String description;

	@Column(name = "price")
	private double price;
	
	
}

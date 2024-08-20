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
public class VisaExtensionSubType extends CommonDTO{
	
	private double price;

	@ManyToOne
	@JoinColumn(name = "visa_id")
	private VisaExtensionType visa;

}

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
public class EmbassyVisaType extends CommonDTO{
	
	private String description;
	
	private double price;
	

	
	
//	@ManyToOne
//	@JoinColumn(name = "visa_service_id")
//	private VisaServices visa;

}

package com.app.shwe.model;

import java.util.List;

import com.app.shwe.utils.StringListConverter;

import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tm30 extends CommonDTO{
	
	private String syskey;
	private String period;
	private String passportBio;
	private String visaPage;
	//@Convert(converter = StringListConverter.class)
	private String duration;
	private String contactNumber;
	private String status;
	@ManyToOne
    @JoinColumn(name = "visa_id")
	private VisaServices visa;

	@ManyToOne
    @JoinColumn(name = "user_id")
	private User user;
    
	@ManyToOne
    @JoinColumn(name = "price_id")
	private Price price;

	

}

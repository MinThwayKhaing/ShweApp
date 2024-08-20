package com.app.shwe.model;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmbassyLetter extends CommonDTO{
	
	private String syskey;
	private String type;
	private String passportBio;
	private String visaPage;
	private String contactNumber;
	private String address;
	private String status;
	
	@ManyToOne
    @JoinColumn(name = "visa_id")
	private VisaServices visa;

	@ManyToOne
    @JoinColumn(name = "user_id")
	private User user;

}
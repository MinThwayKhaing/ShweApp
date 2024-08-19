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
public class Report90Day extends CommonDTO{
	
	private String visaType;
	private String tm6Photo;
	private String expireDatePhoto;
	private String passportBio;
	private String visaPage;
	private String contactNumber;
	@ManyToOne
    @JoinColumn(name = "user_id")
	private User user;
	
	@ManyToOne
    @JoinColumn(name = "visa_id")
	private VisaServices visa;

}

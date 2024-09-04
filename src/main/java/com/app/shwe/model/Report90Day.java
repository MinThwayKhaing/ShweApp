package com.app.shwe.model;

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
public class Report90Day extends CommonDTO {

	private String syskey;
	private String tm6Photo;
	private String expireDatePhoto;
	private String passportBio;
	private String visaPage;
	private String contactNumber;
	private String status;
	private String visaTypeDescription;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne
	@JoinColumn(name = "visa_id")
	private VisaServices visa;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "visa_type", nullable = false)
	private Report90DayVisaType visaType;

}

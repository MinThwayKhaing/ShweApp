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
public class VisaExtension extends CommonDTO {

	private String syskey;
	private String visaType;
	private String passportBio;
	private String visaPage;
	private String contactNumber;
	private String status;
	private String period;
	@ManyToOne
	@JoinColumn(name = "visa_id")
	private VisaServices visa;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

}

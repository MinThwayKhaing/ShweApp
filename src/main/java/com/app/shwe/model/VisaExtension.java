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
public class VisaExtension extends CommonDTO {

	private String syskey;
	private int visaTypeDescription;
	private String passportBio;
	private String visaPage;
	private String contactNumber;
	private String status;
	private String period;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "visa_type", nullable = false)
	private VisaExtensionType visaType;

}

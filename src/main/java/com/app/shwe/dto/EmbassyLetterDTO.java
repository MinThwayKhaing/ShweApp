package com.app.shwe.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmbassyLetterDTO {
	
	private String visaType;
	private String passportBio;
	private String visaPage;
	private String contactNumber;
	private String userName;
	private String status;
	private Date createdDate;
	

}

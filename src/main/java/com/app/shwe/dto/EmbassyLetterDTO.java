package com.app.shwe.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmbassyLetterDTO {
	
	private int visaType;
	private String passportBioPage;
	private String visaPage;
	private String address;
	private String contactNumber;

}

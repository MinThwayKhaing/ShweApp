package com.app.shwe.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmbassyLetterProjectionDTO {
	
	private int id;
	private String order_id;
	private String passportBio;
	private String visaPage;
	private String contactNumber;
	private String user_name;
	private String status;

}

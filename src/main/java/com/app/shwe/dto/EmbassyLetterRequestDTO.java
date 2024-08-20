package com.app.shwe.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmbassyLetterRequestDTO {
	
	private String address;
	private String contactNumber;
	private int visa_id; 
	private int option1;
	private int option2;
	private int option3;
	private int option4;
	private int option5;
	private int option6;
	private int option7;
	private int option8;
	private int option9;
	private int option10;
	private int option11;

}

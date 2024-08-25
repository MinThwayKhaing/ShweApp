package com.app.shwe.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Report90DayRequestDTO {

	private int visaType;
	private String contactNumber;
	private String period;
	private int visa_id;
	private int option1;
	private int option2;
	private int option3;
	private int option4;
	private int option5;
	private int option6;
	private int option7;

}

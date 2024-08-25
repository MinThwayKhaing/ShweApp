package com.app.shwe.dto;

import java.util.List;

import jakarta.persistence.ElementCollection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Tm30RequestDTO {

	private int period;
	private String contactNumber;
	private String duration;
	private int visa_id;
	private int option1;
	private int option2;
	private int option3;

}

package com.app.shwe.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Tm30ResponseDTO {

	private int id;
	private String syskey;
    private String period;
    private String passportBio;
    private String visaPage;
    private String userName;
    private String contactNumber;

}
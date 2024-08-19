package com.app.shwe.dto;

import java.util.Date;
import java.util.TimeZone;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TranslatorRequestDTO {

	private int order_id;
	private String name;
	private String language;
	private String specialist;
	private int translator_id;
	private String status;
	private Date fromDate;
	private Date toDate;
	private Date meetingDate;
	private String meetingPoint;
	private TimeZone meetingTime;
	private String phoneNumber;
	private String usedFor;
	

}

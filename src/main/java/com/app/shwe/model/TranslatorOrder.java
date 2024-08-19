package com.app.shwe.model;

import java.util.Date;
import java.util.TimeZone;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TranslatorOrder extends CommonDTO {

	@ManyToOne
	@JoinColumn(name = "translator_id")
	private Translator translator;
	private String status;
	private String sysKey;
	private Date fromDate;
	private Date toDate;
	private String usedFor;
	private String meetingPoint;

	private Date meetingDate;
	private TimeZone meetingTime;
	private String phoneNumber;
}

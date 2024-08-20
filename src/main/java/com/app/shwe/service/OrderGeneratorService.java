package com.app.shwe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.shwe.repository.CarOrderRepository;
import com.app.shwe.repository.EmbassyLetterRepository;
import com.app.shwe.repository.Report90DayRepository;
import com.app.shwe.repository.VisaExtensionRepository;

import jakarta.transaction.Transactional;

@Service
public class OrderGeneratorService {
	
	
	
	@Autowired
	private Report90DayRepository reportRepo;
	
	@Autowired
	private VisaExtensionRepository visaExtensionRepo;
	
	@Autowired
	private EmbassyLetterRepository embassyRepo;

	private static final String PREFIX = "RP";
	private static final String VISA_EXTENSION_PREFIX = "VE";
	private static final String EMBASSY_EXTENSION_PREFIX = "ER";
	private static final int PAD_LENGTH = 8;
	

	@Transactional
	public String generateReport90DayOrderId() {
		
		// Fetch the maximum sysKey from the database
		String maxSysKey = reportRepo.findMaxSysKey();

		// Remove the prefix and parse the numeric part
		int currentNumber = Integer.parseInt(maxSysKey.substring(PREFIX.length()));

		// Increment the number and format the new ID
		int nextNumber = currentNumber + 1;
		String nextSysKey = PREFIX + String.format("%0" + PAD_LENGTH + "d", nextNumber);

		return nextSysKey;
	}
	
	
	@Transactional
	public String generateVisaExtensionOrderId() {
		
		// Fetch the maximum sysKey from the database
		String maxSysKey = visaExtensionRepo.findMaxSysKey();

		// Remove the prefix and parse the numeric part
		int currentNumber = Integer.parseInt(maxSysKey.substring(VISA_EXTENSION_PREFIX.length()));

		// Increment the number and format the new ID
		int nextNumber = currentNumber + 1;
		String nextSysKey = VISA_EXTENSION_PREFIX + String.format("%0" + PAD_LENGTH + "d", nextNumber);

		return nextSysKey;
	}
	
	@Transactional
	public String generateEmbassyOrderId() {
		
		// Fetch the maximum sysKey from the database
		String maxSysKey = embassyRepo.findMaxSysKey();

		// Remove the prefix and parse the numeric part
		int currentNumber = Integer.parseInt(maxSysKey.substring(EMBASSY_EXTENSION_PREFIX.length()));

		// Increment the number and format the new ID
		int nextNumber = currentNumber + 1;
		String nextSysKey = EMBASSY_EXTENSION_PREFIX + String.format("%0" + PAD_LENGTH + "d", nextNumber);

		return nextSysKey;
	}

}

package com.app.shwe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.shwe.repository.CarOrderRepository;
import com.app.shwe.repository.Report90DayRepository;

import jakarta.transaction.Transactional;

@Service
public class OrderGeneratorService {
	
	
	
	@Autowired
	private Report90DayRepository reportRepo;

	private static final String PREFIX = "RP";
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

}

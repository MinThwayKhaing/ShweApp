package com.app.shwe.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class OrderKeyGeneratorService {
	
	@Autowired
	private VisaOrderRepository visaOrderRepository;

	private static final String PREFIX = "TM";
	private static final int PAD_LENGTH = 8;

	@Transactional
	public String generateVisaOrderId() {
		// Fetch the maximum sysKey from the database
		String maxSysKey = visaOrderRepository.findMaxSysKey();

		// Remove the prefix and parse the numeric part
		int currentNumber = Integer.parseInt(maxSysKey.substring(PREFIX.length()));

		// Increment the number and format the new ID
		int nextNumber = currentNumber + 1;
		String nextSysKey = PREFIX + String.format("%0" + PAD_LENGTH + "d", nextNumber);

		return nextSysKey;
	}

}

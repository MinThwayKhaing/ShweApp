package com.app.shwe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.shwe.repository.TranslatorOrderRepostitory;

import jakarta.transaction.Transactional;

@Service
public class TranslatorOrderIdGenerator {
	
	@Autowired
	private TranslatorOrderRepostitory repo;

	private static final String PREFIX = "TR";
	private static final int PAD_LENGTH = 8;

	@Transactional
	public String generateNextCarOrderId() {
		// Fetch the maximum sysKey from the database
		String maxSysKey = repo.findMaxSysKey();

		// Remove the prefix and parse the numeric part
		int currentNumber = Integer.parseInt(maxSysKey.substring(PREFIX.length()));

		// Increment the number and format the new ID
		int nextNumber = currentNumber + 1;
		String nextSysKey = PREFIX + String.format("%0" + PAD_LENGTH + "d", nextNumber);

		return nextSysKey;
	}

}

package com.app.shwe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.shwe.service.SettingService;

@RestController
@RequestMapping("/api/v1/setting")
public class SettingController {

	@Autowired
	private SettingService settingService;

	@GetMapping("/getAllSetting")
	public ResponseEntity<?> getAllSetting() {
		// Call the service method and return its response
		return settingService.getAllSetting();
	}
}

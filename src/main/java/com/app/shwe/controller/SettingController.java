package com.app.shwe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.shwe.dto.SettingResponseDTO;
import com.app.shwe.service.SettingService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/setting")
@RequiredArgsConstructor
public class SettingController {
	
	@Autowired
	private SettingService settingService;
	
	@GetMapping("/getAllSetting")
	public SettingResponseDTO getAllSetting() {
		return settingService.getAllSetting();
	}

}

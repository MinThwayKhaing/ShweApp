package com.app.shwe.dto;

import java.util.List;
import java.util.Optional;

import com.app.shwe.model.Activity;
import com.app.shwe.model.Article;
import com.app.shwe.model.TermsAndCondition;
import com.app.shwe.model.VisaServices;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SettingResponseDTO {
	
	public TermsAndCondition tnc;
	
	public List<Activity> activity;
	
	public List<VisaServices> visa_services;
	

}

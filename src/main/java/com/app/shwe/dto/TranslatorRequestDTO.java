package com.app.shwe.dto;

import com.app.shwe.model.Specailist;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TranslatorRequestDTO {
	
	private String name;
	private String language;
	private Specailist specialist;

}

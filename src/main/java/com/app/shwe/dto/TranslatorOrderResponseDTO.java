package com.app.shwe.dto;

import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TranslatorOrderResponseDTO {
	
	private int order_id;
	private int translator_id;
	private Date createdDate;
	private String name;
	private String specialist;
	private String status;
	private String image;
	private String chatLink;
	
	

}

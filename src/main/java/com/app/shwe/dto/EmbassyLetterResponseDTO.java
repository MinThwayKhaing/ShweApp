package com.app.shwe.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmbassyLetterResponseDTO {
	
	private EmbassyLetterProjectionDTO embassyLetter;
	
	private List<EmbassyResponseDTO> embassyLetterOrder;

}

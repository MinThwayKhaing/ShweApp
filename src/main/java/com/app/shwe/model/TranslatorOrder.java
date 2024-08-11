package com.app.shwe.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TranslatorOrder extends CommonDTO{
	
	@ManyToOne
	@JoinColumn(name = "translator_id")
	private Translator translator;
	private String status;
	private String sysKey;

}

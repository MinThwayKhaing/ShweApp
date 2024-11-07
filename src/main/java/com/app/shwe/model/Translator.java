package com.app.shwe.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Translator extends CommonDTO {

	@Lob
	@Column(name = "image")
	private String image;

	private String name;

	private String language;

	private String specialist;

	private String chatLink;

	private String phoneNumber;

	@OneToMany(mappedBy = "translator", cascade = CascadeType.ALL)
	private List<DateRange> availability;

}

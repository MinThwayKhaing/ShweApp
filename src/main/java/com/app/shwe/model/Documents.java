package com.app.shwe.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Documents extends CommonDTO{
	
	@Lob
    @Column(name = "images", columnDefinition = "TEXT")
    private String images;
	
	@ManyToOne
	@JoinColumn(name = "main_order_id")
	private MainOrder order;


}

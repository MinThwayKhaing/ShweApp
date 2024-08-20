package com.app.shwe.model;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MainOrder extends CommonDTO {

	private String order_id;
	private int car_type;
	private String car_brand;
	private Date start_date;
	private Date end_date;
	private String period;
	private String status;
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

}

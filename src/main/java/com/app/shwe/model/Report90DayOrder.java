package com.app.shwe.model;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Report90DayOrder extends CommonDTO{
	
	private int order_id;
	private int sub_visa_id;
	private int main_visa_id;

}

package com.app.shwe.model;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmbassyLetterOrder extends CommonDTO{
	
	private int order_id;
	private int sub_visa_id;
	private int main_visa_id;

}

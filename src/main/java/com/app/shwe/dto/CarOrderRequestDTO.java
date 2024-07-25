package com.app.shwe.dto;

import java.util.Date;

import com.app.shwe.model.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CarOrderRequestDTO {
	
	private int carType;
    private Date fromDate;
    private Date toDate;

}

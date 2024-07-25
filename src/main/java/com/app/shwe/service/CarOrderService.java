package com.app.shwe.service;

import org.springframework.stereotype.Service;

import com.app.shwe.dto.CarOrderRequestDTO;
import com.app.shwe.dto.ResponseDTO;
import com.app.shwe.model.CarOrder;

@Service
public class CarOrderService {
	
	public ResponseDTO saveCarOrder(CarOrderRequestDTO dto) {
		ResponseDTO response = new ResponseDTO();
		if(dto.getCarType() != 0 && dto.getFromDate() != null && dto.getToDate() != null) {
			CarOrder order = new CarOrder();
			order.setCarType(dto.getCarType());
			order.setFromDate(dto.getFromDate());
			order.setToDate(dto.getToDate());
		}
		return response;
	}

}

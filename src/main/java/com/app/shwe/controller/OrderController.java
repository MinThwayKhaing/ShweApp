package com.app.shwe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.shwe.dto.CombinedOrderResponseDTO;
import com.app.shwe.dto.SearchDTO;
import com.app.shwe.service.CombinedOrderService;

@RestController
@RequestMapping("/api/v1/all-orders")
public class OrderController {

	@Autowired
	private CombinedOrderService combinedOrderService;

	@GetMapping("/getAllOrders")
	public CombinedOrderResponseDTO getCombinedOrders(@RequestBody SearchDTO dto) {
		CombinedOrderResponseDTO combinedOrders = combinedOrderService.getCombinedOrders(dto);
		return combinedOrders;
	}
	
	@GetMapping("/findOrderByUserId")
	public CombinedOrderResponseDTO findOrderByUserId(@RequestBody SearchDTO dto) {
		CombinedOrderResponseDTO combinedOrders = combinedOrderService.findOrderByUserId(dto);
		return combinedOrders;
	}
	

}

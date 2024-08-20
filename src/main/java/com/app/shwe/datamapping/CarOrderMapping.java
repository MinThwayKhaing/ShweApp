package com.app.shwe.datamapping;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.convert.DtoInstantiatingConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.app.shwe.dto.CarOrderRequestDTO;
import com.app.shwe.model.CarOrder;
import com.app.shwe.model.CarRent;
import com.app.shwe.model.MainOrder;
import com.app.shwe.model.User;
import com.app.shwe.model.VisaServices;
import com.app.shwe.repository.CarRentRepository;
import com.app.shwe.repository.MainOrderRepository;
import com.app.shwe.repository.TranslatorRepository;
import com.app.shwe.repository.UserRepository;
import com.app.shwe.service.CarOrderIdGeneratorService;
import com.app.shwe.service.FileUploadService;
import com.app.shwe.utils.SecurityUtils;

@Service
public class CarOrderMapping {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CarRentRepository carRentRepository;

	@Autowired
	private MainOrderRepository mainOrderRepository;
	
	@Autowired
	private CarOrderIdGeneratorService carOrderIdGeneratorService;

	public CarOrder mapToCarOrder(CarOrderRequestDTO dto) {
		CarOrder carOrder = new CarOrder();
		MainOrder mainOrder = new MainOrder();
		CarRent carRent = carRentRepository.findById(dto.getCarId())
				.orElseThrow(() -> new RuntimeException("CarRent not found for ID: " + dto.getCarId()));
		int userId = userRepository.authUser(SecurityUtils.getCurrentUsername());
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new RuntimeException("User not found for ID: " + userId));
		carOrder.setCarId(carRent);
		carOrder.setStatus(dto.getStatus());
		carOrder.setFromLocation(dto.getFromLocation());
		carOrder.setToLocation(dto.getToLocation());
		carOrder.setPrice(dto.getPrice());
		carOrder.setPickUpDate(dto.getPickUpDate());
		carOrder.setPickUpTime(dto.getPickUpTime());
		carOrder.setFromDate(dto.getFromDate());
		carOrder.setToDate(dto.getToDate());
		carOrder.setCarType(dto.getCarType());
		carOrder.setDriver(dto.isDriver());
		dto.setStatus("Pending");
		carOrder.setPickUpLocation(dto.getPickUpLocation());
		carOrder.setSysKey(carOrderIdGeneratorService.generateNextCarOrderId());
		carOrder.setCreatedDate(new Date());
		carOrder.setCustomerPhoneNumber(dto.getCustomerPhoneNumber());
		carOrder.setCarBrand(carRent.getCarName());
		carOrder.setCarId(carRent);
		carOrder.setCreatedBy(userId);
		mainOrder.setCar_brand(carOrder.getCarBrand());
		mainOrder.setCar_type(carOrder.getCarType());
		mainOrder.setCreatedBy(userId);
        mainOrder.setCreatedDate(new Date());
        mainOrder.setUser(user);
        mainOrder.setOrder_id(carOrder.getSysKey());
        mainOrder.setStatus(carOrder.getStatus());
        mainOrderRepository.save(mainOrder);
		return carOrder;
	}

}
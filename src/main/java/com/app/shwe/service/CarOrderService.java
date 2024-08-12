package com.app.shwe.service;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.app.shwe.datamapping.CarOrderMapping;
import com.app.shwe.dto.CarOrderRequestDTO;
import com.app.shwe.dto.CarOrderResponseDTO;
import com.app.shwe.dto.SearchDTO;
import com.app.shwe.dto.TranslatorOrderResponseDTO;
import com.app.shwe.dto.TranslatorRequestDTO;
import com.app.shwe.model.CarOrder;
import com.app.shwe.model.CarRent;
import com.app.shwe.model.Translator;
import com.app.shwe.repository.CarOrderRepository;
import com.app.shwe.repository.CarRentRepository;
import com.app.shwe.repository.UserRepository;
import com.app.shwe.utils.SecurityUtils;

import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;

@Service
public class CarOrderService {

	@Autowired
	private CarOrderRepository carOrderRepository;

	@Autowired
	private CarOrderMapping carOrderMapping;

	@Autowired
	private CarRentRepository carRentRepository;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CarOrderIdGeneratorService carOrderIdGeneratorService;

	@Transactional
	public ResponseEntity<String> createCarOrder(CarOrderRequestDTO dto) {
		if (dto == null) {
			return new ResponseEntity<>("Request data is null", HttpStatus.BAD_REQUEST);
		}
		dto.setStatus("Pending");
		try {
			CarOrder carOrder = carOrderMapping.mapToCarOrder(dto);
			carOrder.setSysKey(carOrderIdGeneratorService.generateNextCarOrderId());
			carOrderRepository.save(carOrder);

			return new ResponseEntity<>("CarOrder created successfully", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Transactional
	public ResponseEntity<?> getCarOrderById(int id) {
		Optional<CarOrder> carOrder = carOrderRepository.findById(id);
		if (carOrder.isPresent()) {
			return new ResponseEntity<>(carOrder.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>("CarOrder not found", HttpStatus.NOT_FOUND);
		}
	}

	@Transactional
	public ResponseEntity<?> updateCarOrder(int id, CarOrderRequestDTO dto) {
		Optional<CarOrder> carOrderOptional = Optional.ofNullable(getOrderForUpdate(id));
		if (!carOrderOptional.isPresent()) {
			return new ResponseEntity<>("CarOrder not found", HttpStatus.NOT_FOUND);
		}

		try {
			CarOrder carOrder = carOrderOptional.get();
			carOrder = carOrderMapping.mapToCarOrder(dto);

			carOrderRepository.save(carOrder);

			return new ResponseEntity<>("CarOrder updated successfully", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Transactional
	public CarOrder getOrderForUpdate(int orderId) {
		return carOrderRepository.findByIdForUpdate(orderId);
	}

	@Transactional
	public ResponseEntity<?> deleteCarOrder(int id) {
		try {
			carOrderRepository.deleteById(id);
			return new ResponseEntity<>("CarOrder deleted successfully", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// public ResponseEntity<?> getCarOrdersWithSearch(String searchString, Pageable
	// pageable) {
	// try {
	// Page<CarOrderResponseDTO> carOrders =
	// carOrderRepository.findAllWithSearch(searchString, pageable);
	// return new ResponseEntity<>(carOrders, HttpStatus.OK);
	// } catch (Exception e) {
	// return new ResponseEntity<>("Error occurred: " + e.getMessage(),
	// HttpStatus.INTERNAL_SERVER_ERROR);
	// }
	// }

	@Transactional
	public Page<CarOrderResponseDTO> showCarOrders(SearchDTO dto) {
		String searchString = dto.getSearchString();
		int page = (dto.getPage() < 1) ? 0 : dto.getPage() - 1;
		int size = dto.getSize();
		Pageable pageable = PageRequest.of(page, size);
		return carOrderRepository.showCarOrder(searchString, pageable);
	}
	
	@Transactional
	public Page<CarOrderResponseDTO> findOrderByUserId(int id,SearchDTO dto) {
		String searchString = dto.getSearchString();
		int page = (dto.getPage() < 1) ? 0 : dto.getPage() - 1;
		int size = dto.getSize();
		Pageable pageable = PageRequest.of(page, size);
		return carOrderRepository.findOrderByUserId(id,searchString, pageable);
	}

	@Transactional
	public ResponseEntity<String> cancelCarOrder(int id) {
		Optional<CarOrder> carOrderOptional = carOrderRepository.findById(id);
		if (!carOrderOptional.isPresent()) {
			return new ResponseEntity<>("CarOrder not found", HttpStatus.NOT_FOUND);
		}
		try {
			String status = "Cancel Order";
			carOrderRepository.updateOrder(id, status);
			return ResponseEntity.status(HttpStatus.OK).body("Cancel translator order successfully.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error occurred while saving translator: " + e.getMessage());
		}
	}

	@Transactional
	public ResponseEntity<String> confrimOrder(int id, CarOrderRequestDTO request) {
		Optional<CarOrder> carOrderOptional = carOrderRepository.findById(id);
		if (!carOrderOptional.isPresent()) {
			return new ResponseEntity<>("CarOrder not found", HttpStatus.NOT_FOUND);
		}
		CarRent car = carRentRepository.findById(request.getCarId())
				.orElseThrow(() -> new RuntimeException("Car not found for ID: " + request.getCarId()));

		try {
			CarOrder order = carOrderOptional.get();
			order.setCarBrand(request.getCarBrand());
			order.setCarType(request.getCarType());
			order.setFromDate(request.getFromDate());
			order.setDriver(request.isDriver());
			order.setCustomerPhoneNumber(request.getCustomerPhoneNumber());
			order.setUpdatedBy(userRepository.authUser(SecurityUtils.getCurrentUsername()));
			order.setUpdatedDate(new Date());
			order.setPickUpDate(request.getPickUpDate());
			order.setPickUpTime(request.getPickUpTime());
			order.setFromLocation(request.getFromLocation());
			order.setPickUpLocation(request.getPickUpLocation());
			order.setToLocation(request.getToLocation());
			order.setToDate(request.getToDate());
			order.setStatus(request.getStatus());
			order.setCarId(car);
			carOrderRepository.save(order);
			return ResponseEntity.status(HttpStatus.OK).body("Confrim car order successfully.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error occurred while saving translator: " + e.getMessage());
		}
	}

}

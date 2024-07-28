package com.app.shwe.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.app.shwe.dto.CarOrderRequestDTO;
import com.app.shwe.model.CarOrder;
import com.app.shwe.model.User;
import com.app.shwe.repository.CarOrderRepository;
import com.app.shwe.repository.UserRepository;
import com.app.shwe.utils.SecurityUtils;

@Service
public class CarOrderService {

	@Autowired
	private CarOrderRepository carOrderRepository;

	@Autowired
	private UserRepository userRepository;

	public ResponseEntity<?> createCarOrder(CarOrderRequestDTO dto) {
		if (dto == null) {
			return new ResponseEntity<>("Request data is null", HttpStatus.BAD_REQUEST);
		}

		try {
			CarOrder carOrder = new CarOrder();
			carOrder.setCarType(dto.getCarType());
			carOrder.setFromDate(dto.getFromDate());
			carOrder.setToDate(dto.getToDate());
			carOrder.setDriver(dto.isDriver());
			carOrder.setTravelrange(dto.isTravelrange());
			Integer userId = getUserIdFromUsername(SecurityUtils.getCurrentUsername());
			carOrder.setCreatedBy(userId.toString());

			carOrderRepository.save(carOrder);

			return new ResponseEntity<>("CarOrder created successfully", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private int getUserIdFromUsername(String username) {
		// Implement this method to fetch the user ID from the username
		// For example, using a UserRepository to find by username
		int user = userRepository.authUser(username);
		return user;
	}

	public ResponseEntity<?> getCarOrderById(Long id) {
		Optional<CarOrder> carOrder = carOrderRepository.findById(id);
		if (carOrder.isPresent()) {
			return new ResponseEntity<>(carOrder.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>("CarOrder not found", HttpStatus.NOT_FOUND);
		}
	}

	public ResponseEntity<?> updateCarOrder(Long id, CarOrderRequestDTO dto) {
		Optional<CarOrder> carOrderOptional = carOrderRepository.findById(id);
		if (!carOrderOptional.isPresent()) {
			return new ResponseEntity<>("CarOrder not found", HttpStatus.NOT_FOUND);
		}

		try {
			CarOrder carOrder = carOrderOptional.get();
			if (dto.isOrderconfirm()) {
				carOrder.setCar(dto.getCarId());
				carOrder.setOrderConfirm(dto.isOrderconfirm());
			}
			carOrder.setCarType(dto.getCarType());
			carOrder.setFromDate(dto.getFromDate());
			carOrder.setToDate(dto.getToDate());
			carOrder.setDriver(dto.isDriver());
			carOrder.setTravelrange(dto.isTravelrange());
			// Set other necessary fields like cars and user here

			carOrderRepository.save(carOrder);

			return new ResponseEntity<>("CarOrder updated successfully", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<?> deleteCarOrder(Long id) {
		try {
			carOrderRepository.deleteById(id);
			return new ResponseEntity<>("CarOrder deleted successfully", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// public ResponseEntity<?> getAllCarOrders(String searchString, Pageable
	// pageable) {
	// try {
	// Page<CarOrder> carOrders = carOrderRepository.findAllWithSearch(searchString,
	// pageable);
	// return new ResponseEntity<>(carOrders, HttpStatus.OK);
	// } catch (Exception e) {
	// return new ResponseEntity<>("Error occurred: " + e.getMessage(),
	// HttpStatus.INTERNAL_SERVER_ERROR);
	// }
	// }
}

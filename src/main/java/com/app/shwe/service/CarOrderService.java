package com.app.shwe.service;

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
import com.app.shwe.repository.CarOrderRepository;

@Service
public class CarOrderService {

	@Autowired
	private CarOrderRepository carOrderRepository;

	@Autowired
	private CarOrderMapping carOrderMapping;

	// @Autowired
	// private UserRepository userRepository;

	public ResponseEntity<String> createCarOrder(CarOrderRequestDTO dto) {
		if (dto == null) {
			return new ResponseEntity<>("Request data is null", HttpStatus.BAD_REQUEST);
		}
		dto.setStatus("Pending");
		try {
			CarOrder carOrder = carOrderMapping.mapToCarOrder(dto);

			carOrderRepository.save(carOrder);

			return new ResponseEntity<>("CarOrder created successfully", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<?> getCarOrderById(int id) {
		Optional<CarOrder> carOrder = carOrderRepository.findById(id);
		if (carOrder.isPresent()) {
			return new ResponseEntity<>(carOrder.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>("CarOrder not found", HttpStatus.NOT_FOUND);
		}
	}

	public ResponseEntity<?> updateCarOrder(int id, CarOrderRequestDTO dto) {
		Optional<CarOrder> carOrderOptional = carOrderRepository.findById(id);
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
	
	public Page<CarOrderResponseDTO> showCarOrders(int id, SearchDTO dto) {
		String searchString = dto.getSearchString();
		int page = (dto.getPage() < 1) ? 0 : dto.getPage() - 1;
		int size = dto.getSize();
		Pageable pageable = PageRequest.of(page, size);
        return carOrderRepository.showCarOrder(id, searchString, pageable);
    }
	
	public ResponseEntity<String> cancelCarOrder(int id,CarOrderRequestDTO dto){
		if(dto.getStatus().equals("")) {
			throw new IllegalArgumentException("Request and required fields must not be null");
		}
		try {
			String status = dto.getStatus();
			carOrderRepository.cancelCarOrder(id, status);
			return ResponseEntity.status(HttpStatus.OK).body("Cancel translator order successfully.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error occurred while saving translator: " + e.getMessage());
		}
	}
	

}

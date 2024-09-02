package com.app.shwe.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.app.shwe.datamapping.CarRentMapping;
import com.app.shwe.dto.CarRentDTO;
import com.app.shwe.dto.CarRentRequestDTO;
import com.app.shwe.dto.ResponseDTO;
import com.app.shwe.dto.SearchDTO;
import com.app.shwe.dto.TranslatorRequestDTO;
import com.app.shwe.model.CarPrice;
import com.app.shwe.model.CarRent;
import com.app.shwe.model.TranslatorOrder;
import com.app.shwe.repository.CarPriceRepository;
import com.app.shwe.repository.CarRentRepository;
import com.app.shwe.repository.UserRepository;
import com.app.shwe.utils.SecurityUtils;

import jakarta.transaction.Transactional;

@Service
public class CarRentService {

	@Autowired
	private CarRentRepository carRentRepository;

	@Autowired
	private CarPriceRepository carPriceRepository;

	@Autowired
	private FileUploadService fileUploadService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CarRentMapping carRentMapping;

	public ResponseEntity<String> saveCars(MultipartFile carImage, CarRentRequestDTO dto) {
		try {
			if (dto == null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Request data is null.");
			}
			carRentMapping.mapToCarRent(carImage, dto);
			return ResponseEntity.status(HttpStatus.OK).body("Car and price details saved successfully.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error occurred while saving car and price details: " + e.getMessage());
		}
	}

	public Optional<CarRentDTO> findCarById(Integer id) {
		if (id == null) {
			throw new IllegalArgumentException("Id cannot be null");
		}
		Optional<CarRentDTO> cars = carRentRepository.getCarById(id);
		return cars;
	}

	public ResponseEntity<?> getAllCarTypes() {
		try {
			List<Integer> types = carRentRepository.findAllCarTypes();
			return ResponseEntity.status(HttpStatus.OK).body(types);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error occurred while saving car and price details: " + e.getMessage());
		}
	}

	public ResponseEntity<?> findAllBrands() {
		try {
			List<String> brands = carRentRepository.findAllCarNames();
			return ResponseEntity.status(HttpStatus.OK).body(brands);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error occurred while saving car and price details: " + e.getMessage());
		}

	}

	@Transactional
	public ResponseEntity<String> updateCarRent(int id, MultipartFile carImage, CarRentRequestDTO dto) {
		// Find the car rent by ID
		Optional<CarRent> optionalCarRent = carRentRepository.findById(id);
		if (!optionalCarRent.isPresent()) {
			throw new IllegalArgumentException("ID not found");
		}
		try {
			CarRent carRent = optionalCarRent.get();
			if (carImage != null && !carImage.isEmpty()) {
				String imageUrl = fileUploadService.uploadFile(carImage);
				carRent.setImage(imageUrl);
			}
			carRent.setCarName(dto.getCarName());
			carRent.setOwnerName(dto.getOwnerName());
			carRent.setCarNo(dto.getCarNo());
			carRent.setStatus(dto.isStatus());
			carRent.setLicense(dto.getLicense());
			carRent.setDriverName(dto.getDriverName());
			carRent.setDriverPhoneNumber(dto.getDriverPhoneNumber());
			carRent.setCarColor(dto.getCarColor());
			carRent.setCarType(dto.getCarType());
			carRent.setUpdatedDate(new Date());
			carRent.setCreatedBy(userRepository.authUser(SecurityUtils.getCurrentUsername()));

			carRentRepository.save(carRent);
			return ResponseEntity.status(HttpStatus.OK).body("Car and price details updated successfully.");
		} catch (Exception e) {
			return new ResponseEntity<>("Error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<?> deleteCar(int id) {
		// Check if the car exists
		Optional<CarRent> carOptional = carRentRepository.findById(id);

		if (carOptional.isEmpty()) {
			// Car with the given ID not found
			return new ResponseEntity<>("Car with ID " + id + " not found", HttpStatus.NOT_FOUND);
		}

		try {
			CarRent car = carOptional.get();
			car.setDeleteStatus(true); // Set delete status to true
			carRentRepository.save(car); // Save the updated car record
			return new ResponseEntity<>("Car marked as deleted successfully", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public Page<CarRent> showAllCar(String searchString, int page, int size) {
		Pageable pageable = PageRequest.of(page < 1 ? 0 : page - 1, size);
		return carRentRepository.carSimpleSearch(searchString, pageable);
	}

}

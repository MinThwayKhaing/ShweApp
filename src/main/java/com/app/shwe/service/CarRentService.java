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

import com.app.shwe.dto.CarRentDTO;
import com.app.shwe.dto.CarRentRequestDTO;
import com.app.shwe.dto.ResponseDTO;
import com.app.shwe.dto.SearchDTO;
import com.app.shwe.model.CarPrice;
import com.app.shwe.model.CarRent;
import com.app.shwe.repository.CarPriceRepository;
import com.app.shwe.repository.CarRentRepository;
import com.app.shwe.utils.SecurityUtils;

import jakarta.transaction.Transactional;

@Service
public class CarRentService {

	@Autowired
	private CarRentRepository carRentRepository;

	@Autowired
	private CarPriceRepository carPriceRepository;

	public ResponseEntity<String> saveCars(CarRentRequestDTO dto) {
		try {
			if (dto == null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Request data is null.");
			}
			CarRent cars = new CarRent();
			CarPrice price = new CarPrice();
			cars.setOwnerName(dto.getOwnerName());
			cars.setCarName(dto.getCarName());
			cars.setCarNo(dto.getCarNo());
			cars.setStatus(true);
			cars.setLicense(dto.getLicense());
			cars.setDriverPhoneNumber(dto.getDriverPhoneNumber());
			cars.setDriverName(dto.getDriverName());
			cars.setCarColor(dto.getCarColor());
			cars.setCarType(dto.getCarType());
			cars.setCreatedDate(new Date());
			cars.setCreatedBy(SecurityUtils.getCurrentUsername());
			carRentRepository.save(cars);
			price.setCreatedBy(cars.getCreatedBy());
			price.setCreatedDate(new Date());
			price.setInsideTownPrice(dto.getInsideTownPrice());
			price.setOutsideTownPrice(dto.getOutsideTownPrice());
			// price.setWithDriver(dto.isWithDriver());
			price.setCar(cars);
			carPriceRepository.save(price);

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

	@Transactional
	public ResponseEntity<String> updateCarRent(int id, CarRentRequestDTO dto) {
		// Find the car rent by ID
		Optional<CarRent> optionalCarRent = carRentRepository.findById(id);
		if (!optionalCarRent.isPresent()) {
			throw new IllegalArgumentException("ID not found");
		}
		try {
			CarRent carRent = optionalCarRent.get();
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
			carRent.setUpdatedBy(SecurityUtils.getCurrentUsername());

			carRentRepository.save(carRent);

			CarPrice carPrice = carRent.getCarPrice();
			if (carPrice != null) {
				carPrice.setInsideTownPrice(dto.getInsideTownPrice());
				carPrice.setOutsideTownPrice(dto.getOutsideTownPrice());
				carPrice.setUpdatedBy(carRent.getUpdatedBy());
				carPrice.setUpdatedDate(carPrice.getCreatedDate());
			} else {
				carPrice = new CarPrice();
				carPrice.setInsideTownPrice(dto.getInsideTownPrice());
				carPrice.setOutsideTownPrice(dto.getOutsideTownPrice());
				carPrice.setUpdatedBy(carRent.getUpdatedBy());
				carPrice.setUpdatedDate(carPrice.getCreatedDate());
				carPrice.setCar(carRent);
				carRent.setCarPrice(carPrice);
			}
			return ResponseEntity.status(HttpStatus.OK).body("Car and price details updated successfully.");
		} catch (Exception e) {
			return new ResponseEntity<>("Error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<?> deleteCar(int id) {
		// Check if the car exists
		int count = carRentRepository.checkCarById(id);

		if (count == 0) {
			// Car with the given ID not found
			return new ResponseEntity<>("Car with ID " + id + " not found", HttpStatus.NOT_FOUND);
		}

		try {
			carRentRepository.deleteById(id);
			return new ResponseEntity<>("Car Details deleted successfully", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public Page<CarRent> showAllCar(SearchDTO search) {
		String searchString = search.getSearchString();
		int page = (search.getPage() < 1) ? 0 : search.getPage() - 1;
		int size = search.getSize();
		Pageable pageable = PageRequest.of(page, size);
		return carRentRepository.carSimpleSearch(searchString, pageable);
	}

}

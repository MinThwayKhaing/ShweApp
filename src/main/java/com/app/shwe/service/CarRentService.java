package com.app.shwe.service;

import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

import org.apache.catalina.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.app.shwe.dto.CarRentDTO;
import com.app.shwe.dto.CarRentRequestDTO;
import com.app.shwe.dto.ResponseDTO;
import com.app.shwe.dto.SearchDTO;
import com.app.shwe.model.CarPrice;
import com.app.shwe.model.CarRent;
import com.app.shwe.userRepository.CarPriceRepository;
import com.app.shwe.userRepository.CarRentRepository;
import com.app.shwe.utils.SecurityUtils;

import jakarta.transaction.Transactional;

@Service
public class CarRentService {

	@Autowired
	private CarRentRepository carRentRepository;

	@Autowired
	private CarPriceRepository carPriceRepository;

	public ResponseDTO saveCars(CarRentRequestDTO dto) {
		ResponseDTO response = new ResponseDTO();
		SecurityUtils utils = new SecurityUtils();
		CarRent cars = new CarRent();
		CarPrice price = new CarPrice();
		if (!dto.getCarName().isEmpty() && !dto.getOwnerName().isEmpty() && !dto.getCarNo().isEmpty()
				&& !dto.getLicense().isEmpty() && !dto.getDriverName().isEmpty()) {
			cars.setOwnerName(dto.getOwnerName());
			cars.setCarName(dto.getCarName());
			cars.setCarNo(dto.getCarNo());
			cars.setStatus(true);
			cars.setLicense(dto.getLicense());
			cars.setReview(dto.getReview());
			cars.setDriverName(dto.getDriverName());
			cars.setCarColor(dto.getCarColor());
			cars.setCarType(dto.getCarType());
			cars.setCreatedDate(new Date());
			cars.setCreatedBy(utils.getCurrentUsername());
			carRentRepository.save(cars);
			price.setCreatedBy(utils.getCurrentUsername());
			price.setCreatedDate(new Date());
			price.setInsideTownPrice(dto.getInsideTownPrice());
			price.setOutsideTownPrice(dto.getOutsideTownPrice());
			price.setWithDriver(dto.isWithDriver());
			price.setCar(cars);
			carPriceRepository.save(price);
			response.setStatus(200);
			response.setDescription("Save Car successfully");
		} else {
			response.setStatus(403);
			response.setDescription("Missing some information to save car");
		}

		return response;
	}

	public Optional<CarRentDTO> findCarById(int id) {
		Optional<CarRentDTO> cars = carRentRepository.getCarById(id);
		if (cars.isPresent()) {
			if (cars.isEmpty() || cars.get() == null) {
				return Optional.empty();
			}
		}
		return cars;
	}

	@Transactional
	public ResponseDTO updateCarRent(int id, CarRentRequestDTO dto) {
		ResponseDTO response = new ResponseDTO();
		SecurityUtils utils = new SecurityUtils();
		Optional<CarRent> cars = carRentRepository.findById(id);
		if (cars.isPresent()) {
			CarRent carRent = cars.get();
			carRent.setCarName(dto.getCarName());
			carRent.setOwnerName(dto.getOwnerName());
			carRent.setCarNo(dto.getCarNo());
			carRent.setStatus(dto.isStatus());
			carRent.setLicense(dto.getLicense());
			carRent.setReview(dto.getReview());
			carRent.setDriverName(dto.getDriverName());
			carRent.setCarColor(dto.getCarColor());
			carRent.setCarType(dto.getCarType());
			carRent.setUpdatedDate(new Date());
			carRent.setUpdatedBy(utils.getCurrentUsername());
			carRentRepository.save(carRent);
			CarPrice carPrice = carRent.getCarPrice();
			if (carPrice != null) {
				carPrice.setInsideTownPrice(dto.getInsideTownPrice());
				carPrice.setOutsideTownPrice(dto.getOutsideTownPrice());
				carPrice.setWithDriver(dto.isWithDriver());
			} else {
				carPrice = new CarPrice();
				carPrice.setInsideTownPrice(dto.getInsideTownPrice());
				carPrice.setOutsideTownPrice(dto.getOutsideTownPrice());
				carPrice.setWithDriver(dto.isWithDriver());
				carPrice.setCar(carRent);
				carRent.setCarPrice(carPrice);
			}

			response.setStatus(200);
			response.setDescription("Update Car Successfully");
		} else {
			response.setStatus(403);
			response.setDescription("Update Fail");
		}

		return response;
	}

	public ResponseDTO deleteCar(int id) {
		ResponseDTO response = new ResponseDTO();
		int result = carRentRepository.checkCarById(id);
		if (result == 1) {
			carRentRepository.deleteById(id);
			response.setStatus(200);
			response.setDescription("Delete Car Successfully");
		} else {
			response.setStatus(403);
			response.setDescription("Car is not found");
		}
		return response;
	}

	public Page<CarRent> showAllCar(SearchDTO search) {
		String searchString = search.getSearchString();
		int page = (search.getPage() < 1) ? 0 : search.getPage() - 1;
		int size = search.getSize();
		Pageable pageable = PageRequest.of(page, size);
		return carRentRepository.carSimpleSearch(searchString, pageable);
	}

}

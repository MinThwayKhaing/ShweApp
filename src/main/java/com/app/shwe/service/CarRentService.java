package com.app.shwe.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.app.shwe.dto.CarRentRequestDTO;
import com.app.shwe.dto.ResponseDTO;
import com.app.shwe.dto.SearchDTO;
import com.app.shwe.model.CarRent;
import com.app.shwe.userRepository.CarRentRepository;

@Service
public class CarRentService {

	@Autowired
	private CarRentRepository carRentRepository;

	public ResponseDTO saveCars(CarRentRequestDTO dto) {
		ResponseDTO response = new ResponseDTO();
		CarRent cars = new CarRent();
		if (!dto.getCarName().isEmpty() && !dto.getOwnerName().isEmpty() && !dto.getCarNo().isEmpty()) {
			cars.setOwnerName(dto.getOwnerName());
			cars.setCarName(dto.getCarName());
			cars.setCarNo(dto.getCarNo());
			cars.setStatus(true);
			carRentRepository.save(cars);
			response.setStatus(200);
			response.setDescription("Save Car successfully");
		} else {
			response.setStatus(403);
			response.setDescription("Missing some information to save car");
		}

		return response;
	}

	public Optional<CarRent> findCarById(int id) {
		Optional<CarRent> cars = carRentRepository.findById(id);
		if (cars.isPresent()) {
			if (cars.isEmpty() || cars.get() == null) {
				return Optional.empty();
			}
		}
		return cars;
	}

	public ResponseDTO updateCarRent(int id, CarRentRequestDTO dto) {
		ResponseDTO response = new ResponseDTO();
		Optional<CarRent> cars = carRentRepository.findById(id);
		if (cars.isPresent()) {
			CarRent carRent = cars.get();
			carRent.setCarName(dto.getCarName());
			carRent.setOwnerName(dto.getOwnerName());
			carRent.setCarNo(dto.getCarNo());
			carRent.setStatus(dto.isStatus());
			carRentRepository.save(carRent);
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

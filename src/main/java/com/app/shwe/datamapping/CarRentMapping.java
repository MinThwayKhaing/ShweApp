package com.app.shwe.datamapping;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.app.shwe.dto.CarRentRequestDTO;
import com.app.shwe.model.CarPrice;
import com.app.shwe.model.CarRent;
import com.app.shwe.repository.CarPriceRepository;
import com.app.shwe.repository.CarRentRepository;
import com.app.shwe.repository.UserRepository;
import com.app.shwe.service.FileUploadService;
import com.app.shwe.utils.SecurityUtils;

@Service
public class CarRentMapping {

	@Autowired
	private FileUploadService fileUploadService;

	@Autowired
	private CarRentRepository carRentRepository;

	@Autowired
	private CarPriceRepository carPriceRepository;

	@Autowired
	private UserRepository userRepository;

	public CarRent mapToCarRent(MultipartFile carImage, CarRentRequestDTO dto) {
		CarRent cars = new CarRent();
		String imageUrl = fileUploadService.uploadFile(carImage);
		CarPrice price = new CarPrice();
		cars.setOwnerName(dto.getOwnerName());
		cars.setCarName(dto.getCarName());
		cars.setCarNo(dto.getCarNo());
		cars.setImage(imageUrl);
		cars.setStatus(true);
		cars.setLicense(dto.getLicense());
		cars.setDriverPhoneNumber(dto.getDriverPhoneNumber());
		cars.setDriverName(dto.getDriverName());
		cars.setCarColor(dto.getCarColor());
		cars.setCarType(dto.getCarType());
		cars.setCreatedDate(new Date());
		cars.setCreatedBy(userRepository.authUser(SecurityUtils.getCurrentUsername()));
		carRentRepository.save(cars);
		price.setCreatedBy(cars.getCreatedBy());
		price.setCreatedDate(new Date());
		price.setInsideTownPrice(dto.getInsideTownPrice());
		price.setOutsideTownPrice(dto.getOutsideTownPrice());
		// price.setWithDriver(dto.isWithDriver());
		price.setCar(cars);
		carPriceRepository.save(price);
		return cars;
	}

}

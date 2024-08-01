package com.app.shwe.datamapping;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.app.shwe.dto.CarOrderRequestDTO;
import com.app.shwe.model.CarOrder;
import com.app.shwe.model.CarRent;
import com.app.shwe.repository.CarRentRepository;
import com.app.shwe.repository.TranslatorRepository;
import com.app.shwe.repository.UserRepository;
import com.app.shwe.service.FileUploadService;
import com.app.shwe.utils.SecurityUtils;

@Service
public class CarOrderMapping {

    @Autowired
    UserRepository userRepository;
    
    @Autowired
    private CarRentRepository carRentRepository;
    

    public CarOrder mapToCarOrder(CarOrderRequestDTO dto) {
        CarOrder carOrder = new CarOrder();
        CarRent carRent = carRentRepository.findById(dto.getCarId())
                .orElseThrow(() -> new RuntimeException("CarRent not found for ID: " + dto.getCarId()));
        carOrder.setCarId(carRent);
        carOrder.setStatus(dto.getStatus());
        carOrder.setFromLocation(dto.getFromLocation());
        carOrder.setToLocation(dto.getToLocation());
        carOrder.setPickUpDate(dto.getPickUpDate());
        carOrder.setPickUpTime(dto.getPickUpTime());
        carOrder.setFromDate(dto.getFromDate());
        carOrder.setToDate(dto.getToDate());
        carOrder.setCarType(dto.getCarType());
        carOrder.setDriver(dto.isDriver());
        carOrder.setCreatedDate(new Date());
        carOrder.setCustomerPhoneNumber(dto.getCustomerPhoneNumber());
        carOrder.setCarBrand(carRent.getCarName());
        carOrder.setCarId(carRent);

        int userId = userRepository.authUser(SecurityUtils.getCurrentUsername());
        carOrder.setCreatedBy(userId);

        return carOrder;
    }
    
   
}
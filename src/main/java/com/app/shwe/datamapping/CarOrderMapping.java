package com.app.shwe.datamapping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.shwe.dto.CarOrderRequestDTO;
import com.app.shwe.model.CarOrder;
import com.app.shwe.repository.UserRepository;
import com.app.shwe.service.UserService;
import com.app.shwe.utils.SecurityUtils;

@Service
public class CarOrderMapping {

    @Autowired
    UserRepository userRepository;

    public CarOrder mapToCarOrder(CarOrderRequestDTO dto) {
        CarOrder carOrder = new CarOrder();
        carOrder.setCarId(dto.getCarId());
        carOrder.setStatus(dto.getStatus());
        carOrder.setFromLocation(dto.getFromLocation());
        carOrder.setToLocation(dto.getToLocation());
        carOrder.setPickUpDate(dto.getPickUpDate());
        carOrder.setPickUpTime(dto.getPickUpTime());
        carOrder.setFromDate(dto.getFromDate());
        carOrder.setToDate(dto.getToDate());
        carOrder.setCarType(dto.getCarType());
        carOrder.setDriver(dto.isDriver());
        carOrder.setCustomerPhoneNumber(dto.getCustomerPhoneNumber());
        carOrder.setCarBrand(dto.getCarBrand());
        carOrder.setCarId(dto.getCarId());

        int userId = userRepository.authUser(SecurityUtils.getCurrentUsername());
        carOrder.setCreatedBy(userId);

        return carOrder;
    }
}
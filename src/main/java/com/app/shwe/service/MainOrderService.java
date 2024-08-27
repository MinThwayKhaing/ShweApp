package com.app.shwe.service;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.app.shwe.dto.MainOrderDTO;
import com.app.shwe.model.MainOrder;
import com.app.shwe.repository.MainOrderRepository;
import com.app.shwe.utils.SecurityUtils;
import com.app.shwe.repository.UserRepository;

@Service
public class MainOrderService {

    @Autowired
    private MainOrderRepository mainOrderRepository;
    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<?> getMainOrdersPaginated(int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<MainOrder> mainOrdersPage = mainOrderRepository.findAll(pageable);
            return ResponseEntity.status(HttpStatus.OK).body(mainOrdersPage.map(this::convertToDTO));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred  " + e.getMessage());
        }
    }

    public ResponseEntity<?> getMainOrdersPaginatedwithCurrentUser(int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            int userId = userRepository.authUser(SecurityUtils.getCurrentUsername());
            Page<MainOrder> mainOrdersPage = mainOrderRepository.findByUserId(pageable, userId);

            return ResponseEntity.status(HttpStatus.OK).body(mainOrdersPage.map(this::convertToDTO));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred  " + e.getMessage());
        }
    }

    private MainOrderDTO convertToDTO(MainOrder mainOrder) {
        return new MainOrderDTO(
                mainOrder.getOrder_id(),
                mainOrder.getSys_key(),
                mainOrder.getCar_type(),
                mainOrder.getCar_brand(),
                mainOrder.getStart_date(),
                mainOrder.getEnd_date(),
                mainOrder.getPeriod(),
                mainOrder.getStatus());
    }
}
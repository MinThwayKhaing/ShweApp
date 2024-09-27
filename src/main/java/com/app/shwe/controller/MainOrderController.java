package com.app.shwe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.shwe.service.MainOrderService;

@RestController
@RequestMapping("/api/v1/main-orders")

public class MainOrderController {

    @Autowired
    private MainOrderService mainOrderService;

    @GetMapping("/paginated")
    public ResponseEntity<?> getMainOrdersPaginated(
            @RequestParam(required = false) String searchString,
            @RequestParam String status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        return mainOrderService.getMainOrdersPaginated(searchString, status, page, size);
    }

    @GetMapping
    public ResponseEntity<?> getMainOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return mainOrderService.getMainOrdersPaginated(page, size);
    }

    @GetMapping("/current-user")
    public ResponseEntity<?> getMainOrdersPaginatedwithCurrentUser(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return mainOrderService.getMainOrdersPaginatedwithCurrentUser(page, size);
    }
}

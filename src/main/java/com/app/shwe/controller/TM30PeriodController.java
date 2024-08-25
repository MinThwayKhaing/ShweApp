package com.app.shwe.controller;



import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.app.shwe.service.TM30PeriodService;

@RestController
@RequestMapping("/api/v1/tm30period")
public class TM30PeriodController {

    @Autowired
    private TM30PeriodService tm30PeriodService;

    @GetMapping
    public ResponseEntity<?> getAllTM30Periods() {
        return tm30PeriodService.getAllTM30Periods();
    }

}

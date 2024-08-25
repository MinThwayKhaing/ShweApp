package com.app.shwe.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.app.shwe.dto.TM30PeriodDTO;
import com.app.shwe.repository.TM30PeriodRepository;

@Service
public class TM30PeriodService {

    @Autowired
    private TM30PeriodRepository tm30PeriodRepository;

    public ResponseEntity<List<TM30PeriodDTO>> getAllTM30Periods() {
        List<TM30PeriodDTO> periods = tm30PeriodRepository.findAllTM30Periods();
        return new ResponseEntity<>(periods, HttpStatus.OK);
    }

    public ResponseEntity<?> getTM30PeriodById(int id) {
        TM30PeriodDTO period = tm30PeriodRepository.findTM30PeriodById(id);
        if (period == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Period Is Null");
        }
        return new ResponseEntity<>(period, HttpStatus.OK);
    }
}

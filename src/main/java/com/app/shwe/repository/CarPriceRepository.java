package com.app.shwe.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.shwe.model.CarPrice;

public interface CarPriceRepository extends JpaRepository<CarPrice, Integer> {

}

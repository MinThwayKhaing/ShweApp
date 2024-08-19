package com.app.shwe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.shwe.model.Report90Day;

@Repository
public interface Report90DayRepository extends JpaRepository<Report90Day, Integer>{

}

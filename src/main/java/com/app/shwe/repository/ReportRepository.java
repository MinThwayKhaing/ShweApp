package com.app.shwe.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.shwe.model.Report;

public interface ReportRepository extends JpaRepository<Report, Integer> {

}

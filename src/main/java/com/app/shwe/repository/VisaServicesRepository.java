package com.app.shwe.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.app.shwe.model.VisaServices;

public interface VisaServicesRepository extends JpaRepository<VisaServices, Integer>{
	
	@Query("SELECT v FROM VisaServices v")
	Page<VisaServices> getAllVisa(@Param("searchString") String searchString, Pageable pageable);
	
	@Query("SELECT COUNT(v) FROM VisaServices v WHERE v.id = :id")
	int checkVisaById(@Param("id") int id);

}

package com.app.shwe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.app.shwe.model.VisaOrder;

public interface VisaOrderRepository extends JpaRepository<VisaOrder, Integer>{

	 @Query("SELECT COALESCE(MAX(v.order_id), 'TM00000000') FROM VisaOrder v")
	    String findMaxSysKey();
}

package com.app.shwe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.app.shwe.model.VisaOrder;

public interface VisaOrderRepository extends JpaRepository<VisaOrder, Integer>{

	 @Query("SELECT COALESCE(MAX(t.syskey), 'TM00000000') FROM Tm30 t")
	    String findMaxSysKey();
}

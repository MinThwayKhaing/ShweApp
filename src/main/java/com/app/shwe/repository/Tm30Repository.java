package com.app.shwe.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.app.shwe.model.Tm30;

public interface Tm30Repository extends JpaRepository<Tm30, Integer>{
	
	@Query("SELECT t FROM Tm30 t ORDER BY t.createdDate DESC")
	Page<Tm30> getAllTm30(Pageable pageable);
	
	@Query("SELECT COUNT(t) FROM Tm30 t WHERE t.id = :id")
	int checkTm30ById(@Param("id") int id);

}

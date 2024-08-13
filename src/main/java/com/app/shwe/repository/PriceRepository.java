package com.app.shwe.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.app.shwe.model.Price;

@Repository
public interface PriceRepository extends JpaRepository<Price,Integer>{
    
    @Query("SELECT p FROM Price p ORDER BY p.createdDate DESC")
	Page<Price> getAllPrice(Pageable pageable);

    @Query("SELECT COUNT(p) FROM Price p WHERE p.id = :id")
	int checkPriceById(@Param("id") int id);
}

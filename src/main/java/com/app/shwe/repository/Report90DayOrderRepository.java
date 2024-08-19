package com.app.shwe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.app.shwe.model.Report90DayOrder;

import jakarta.transaction.Transactional;

@Repository
public interface Report90DayOrderRepository extends JpaRepository<Report90DayOrder, Integer>{
	
	@Modifying
    @Transactional
    @Query("DELETE FROM Report90DayOrder r WHERE r.order_id = :order_id")
    void deleteOrderById(int order_id);

}

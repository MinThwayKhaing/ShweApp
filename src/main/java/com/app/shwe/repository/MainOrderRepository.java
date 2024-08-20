package com.app.shwe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.shwe.model.MainOrder;

@Repository
public interface MainOrderRepository  extends JpaRepository<MainOrder, Integer>{

	
}

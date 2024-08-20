package com.app.shwe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.shwe.model.VisaExtensionOrder;

@Repository
public interface VisaExtensionOrderRepository extends JpaRepository<VisaExtensionOrder, Integer>{

}

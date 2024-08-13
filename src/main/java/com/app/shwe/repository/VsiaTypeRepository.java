package com.app.shwe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.shwe.model.VisaType;

@Repository
public interface VsiaTypeRepository extends JpaRepository<VisaType,Integer>{
    
}

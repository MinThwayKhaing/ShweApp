package com.app.shwe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.shwe.model.SubVisaType;

@Repository
public interface SubVisaTypeRepository extends JpaRepository<SubVisaType,Integer> {
    
}

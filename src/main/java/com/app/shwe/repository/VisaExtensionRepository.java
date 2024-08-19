package com.app.shwe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.shwe.model.VisaExtension;

@Repository
public interface VisaExtensionRepository extends JpaRepository<VisaExtension, Integer>{

}

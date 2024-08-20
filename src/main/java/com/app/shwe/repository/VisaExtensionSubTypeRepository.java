package com.app.shwe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.app.shwe.model.VisaExtensionSubType;

import jakarta.transaction.Transactional;

@Repository
public interface VisaExtensionSubTypeRepository extends JpaRepository<VisaExtensionSubType, Integer>{
	
	@Modifying
    @Transactional
    @Query("DELETE FROM VisaExtensionSubType s WHERE s.visa.id = :visaId")
    void deleteSubVisaTypesByVisaId(int visaId);

}

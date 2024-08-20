package com.app.shwe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.app.shwe.model.EmbassySubVisaType;

import jakarta.transaction.Transactional;

@Repository
public interface EmbassySubVisaTypeRepository extends JpaRepository<EmbassySubVisaType, Integer>{
	
	@Modifying
    @Transactional
    @Query("DELETE FROM EmbassySubVisaType s WHERE s.visa.id = :visaId")
    void deleteSubVisaTypesByVisaId(int visaId);

}
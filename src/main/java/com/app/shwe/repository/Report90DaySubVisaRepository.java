package com.app.shwe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.app.shwe.model.Report90DaySubVisaType;

import jakarta.transaction.Transactional;

@Repository
public interface Report90DaySubVisaRepository extends JpaRepository<Report90DaySubVisaType, Integer>{
	
	@Modifying
    @Transactional
    @Query("DELETE FROM Report90DaySubVisaType s WHERE s.visa.id = :visaId")
    void deleteSubVisaTypesByVisaId(int visaId);

}

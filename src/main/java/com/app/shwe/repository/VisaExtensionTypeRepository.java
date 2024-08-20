package com.app.shwe.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.app.shwe.dto.Report90DayTypeResponseType;
import com.app.shwe.dto.VisaExtensionTypeResponseDTO;
import com.app.shwe.model.VisaExtensionType;

import jakarta.transaction.Transactional;

public interface VisaExtensionTypeRepository extends JpaRepository<VisaExtensionType, Integer>{
	
	
	@Query("SELECT new com.app.shwe.dto.VisaExtensionTypeResponseDTO(v.id,v.description,s.price) "
			+ "FROM VisaExtensionType v JOIN VisaExtensionSubType s ON s.visa.id = v.id WHERE v.description = :type")
	List<VisaExtensionTypeResponseDTO> findVisaByType(String type);
	
	@Query("SELECT new com.app.shwe.dto.VisaExtensionTypeResponseDTO(v.id,v.description,s.price) "
			+ "FROM VisaExtensionType v JOIN VisaExtensionSubType s ON s.visa.id = v.id")
	List<VisaExtensionTypeResponseDTO> findAllVisaType();
	
	@Query("SELECT COUNT(v) FROM VisaExtensionType v WHERE v.id = :id")
	int checkVisaTypeById(@Param("id") int id);
	
	@Modifying
    @Transactional
    @Query("DELETE FROM VisaExtensionType v WHERE v.id = :id")
    void deleteVisaTypeById(int id);

}

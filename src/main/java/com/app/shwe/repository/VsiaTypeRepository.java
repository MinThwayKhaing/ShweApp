package com.app.shwe.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.app.shwe.dto.UserReportDTO;
import com.app.shwe.dto.VisaTypeResponseDTO;
import com.app.shwe.model.VisaType;

import jakarta.transaction.Transactional;

@Repository
public interface VsiaTypeRepository extends JpaRepository<VisaType,Integer>{
    
    @Query("SELECT new com.app.shwe.dto.VisaTypeResponseDTO(v.id,v.description,s.price,s.duration) "
			+ "FROM VisaType v JOIN SubVisaType s ON s.visa.id = v.id WHERE v.description = :type")
	List<VisaTypeResponseDTO> findVisaByType(String type);
    
    
    @Query("SELECT new com.app.shwe.dto.VisaTypeResponseDTO(v.id,v.description,s.price,s.duration) "
			+ "FROM VisaType v JOIN SubVisaType s ON s.visa.id = v.id")
	List<VisaTypeResponseDTO> findAllVisaType();
    
    @Query("SELECT COUNT(v) FROM VisaType v WHERE v.id = :id")
	int checkVisaTypeById(@Param("id") int id);
    
    @Modifying
    @Transactional
    @Query("DELETE FROM VisaType v WHERE v.id = :id")
    void deleteVisaTypeById(int id);
}

package com.app.shwe.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.app.shwe.dto.UserReportDTO;
import com.app.shwe.dto.VisaTypeResponseDTO;
import com.app.shwe.model.VisaType;

@Repository
public interface VsiaTypeRepository extends JpaRepository<VisaType,Integer>{
    
    @Query("SELECT new com.app.shwe.dto.VisaTypeResponseDTO(v.visaType,s.price,s.duration) "
			+ "FROM VisaType v JOIN SubVisaType s ON s.visa.id = v.id WHERE v.visaType = :type")
	List<VisaTypeResponseDTO> findVisaByType(String type);
}

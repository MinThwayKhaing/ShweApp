package com.app.shwe.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.app.shwe.dto.EmbassyVisaTypeResponseDTO;
import com.app.shwe.dto.Report90DayTypeResponseType;
import com.app.shwe.model.EmbassyVisaType;
import com.app.shwe.model.VisaExtensionType;

import jakarta.transaction.Transactional;

@Repository
public interface EmbassyVisaTypeRepository extends JpaRepository<EmbassyVisaType, Integer>{
	
	@Query("SELECT new com.app.shwe.dto.EmbassyVisaTypeResponseDTO(v.id,v.description,s.price) "
			+ "FROM EmbassyVisaType v JOIN EmbassySubVisaType s ON s.visa.id = v.id WHERE v.description = :type")
	List<EmbassyVisaTypeResponseDTO> findVisaByType(String type);
	
	
	@Query("SELECT new com.app.shwe.dto.EmbassyVisaTypeResponseDTO(v.id,v.description,s.price) "
			+ "FROM EmbassyVisaType v JOIN EmbassySubVisaType s ON s.visa.id = v.id")
	List<EmbassyVisaTypeResponseDTO> findAllVisaType();
	
	@Query("SELECT COUNT(v) FROM EmbassyVisaType v WHERE v.id = :id")
	int checkVisaTypeById(@Param("id") int id);
	
	@Modifying
	@Transactional
	@Query("DELETE FROM EmbassyVisaType v WHERE v.id = :id")
	void deleteVisaTypeById(int id);
	
	@Query(value = "SELECT * FROM embassy_visa_type WHERE id = :id", nativeQuery = true)
	EmbassyVisaType findEmbassyVisaTypeById(@Param("id") int id);

}

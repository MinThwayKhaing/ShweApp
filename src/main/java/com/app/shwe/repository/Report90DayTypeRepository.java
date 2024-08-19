package com.app.shwe.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.app.shwe.dto.Report90DayTypeResponseType;
import com.app.shwe.dto.VisaTypeResponseDTO;
import com.app.shwe.model.Report90DayVisaType;

import jakarta.transaction.Transactional;

public interface Report90DayTypeRepository extends JpaRepository<Report90DayVisaType, Integer> {

	@Query("SELECT new com.app.shwe.dto.Report90DayTypeResponseType(v.id,v.description,s.price) "
			+ "FROM Report90DayVisaType v JOIN Report90DaySubVisaType s ON s.visa.id = v.id WHERE v.description = :type")
	List<Report90DayTypeResponseType> findVisaByType(String type);

	@Query("SELECT new com.app.shwe.dto.Report90DayTypeResponseType(v.id,v.description,s.price) "
			+ "FROM Report90DayVisaType v JOIN Report90DaySubVisaType s ON s.visa.id = v.id")
	List<Report90DayTypeResponseType> findAllVisaType();

	@Query("SELECT COUNT(v) FROM Report90DayVisaType v WHERE v.id = :id")
	int checkVisaTypeById(@Param("id") int id);

	@Modifying
	@Transactional
	@Query("DELETE FROM VisaType v WHERE v.id = :id")
	void deleteVisaTypeById(int id);
}

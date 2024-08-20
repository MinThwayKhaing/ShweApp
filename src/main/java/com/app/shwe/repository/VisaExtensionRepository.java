package com.app.shwe.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.app.shwe.dto.Report90DayProjectionDTO;
import com.app.shwe.dto.Report90DayTypeResponseDTO;
import com.app.shwe.dto.VisaExtensionOrderResponseDTO;
import com.app.shwe.dto.VisaExtensionProjectionDTO;
import com.app.shwe.dto.VisaExtensionResponseDTO;
import com.app.shwe.dto.VisaExtensionTypeResponseDTO;
import com.app.shwe.model.VisaExtension;

import jakarta.transaction.Transactional;

@Repository
public interface VisaExtensionRepository extends JpaRepository<VisaExtension, Integer>{
	
	@Query("SELECT new com.app.shwe.dto.VisaExtensionTypeResponseDTO(v.id,v.description,s.price) "
			+ "FROM Report90DayVisaType v JOIN Report90DaySubVisaType s ON s.visa.id = v.id")
	List<VisaExtensionTypeResponseDTO> findAllVisaType();
	
	@Query("SELECT new com.app.shwe.dto.VisaExtensionProjectionDTO(v.id,v.syskey,v.passportBio, v.visaPage, v.contactNumber,v.user.userName,v.status) FROM VisaExtension v WHERE v.user.id = :id ORDER BY v.id DESC")
	List<VisaExtensionProjectionDTO> getVisaExtensionOrderByUserId(int id);
	
	@Query("SELECT new com.app.shwe.dto.VisaExtensionOrderResponseDTO(vo.order_id,vs.serviceName, vo.main_visa_id, vo.sub_visa_id, vt.description, svt.price) "
			+ "FROM VisaExtensionType vt "
			+ "JOIN VisaServices vs ON vt.visa.id = vs.id "
			+ "JOIN VisaExtensionOrder vo ON vo.main_visa_id = vs.id "
			+ "JOIN VisaExtensionSubType svt ON svt.id = vo.sub_visa_id "
			+ "WHERE vo.order_id = :orderId")
	List<VisaExtensionOrderResponseDTO> getVisaOrderByOrderId(@Param("orderId") int orderId);
	
	@Modifying
	@Transactional
	@Query("UPDATE VisaExtension v SET v.status = :status WHERE v.id = :id")
	void cancelOrder(@Param("id") int id, @Param("status") String status);
	
	@Query("SELECT COALESCE(MAX(r.syskey), 'VE00000000') FROM VisaExtension r")
    String findMaxSysKey();

}
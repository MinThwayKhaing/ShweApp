package com.app.shwe.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.app.shwe.dto.EmbassyLetterProjectionDTO;
import com.app.shwe.dto.EmbassyLetterResponseDTO;
import com.app.shwe.dto.EmbassyResponseDTO;
import com.app.shwe.dto.VisaExtensionOrderResponseDTO;
import com.app.shwe.model.EmbassyLetter;

import jakarta.transaction.Transactional;

@Repository
public interface EmbassyLetterRepository extends JpaRepository<EmbassyLetter, Integer>{
	
	@Query("SELECT new com.app.shwe.dto.EmbassyLetterProjectionDTO(v.id,v.syskey,v.passportBio, v.visaPage, v.contactNumber,v.user.userName,v.status) FROM EmbassyLetter v WHERE v.user.id = :id ORDER BY v.id DESC")
	List<EmbassyLetterProjectionDTO> getEmbassyLetterByUserId(int id);
	
	@Query("SELECT new com.app.shwe.dto.EmbassyResponseDTO(vo.order_id,vs.serviceName, vo.main_visa_id, vo.sub_visa_id, vt.description, svt.price) "
			+ "FROM EmbassyVisaType vt "
			+ "JOIN VisaServices vs ON vt.visa.id = vs.id "
			+ "JOIN EmbassyLetterOrder vo ON vo.main_visa_id = vs.id "
			+ "JOIN EmbassySubVisaType svt ON svt.id = vo.sub_visa_id "
			+ "WHERE vo.order_id = :orderId")
	List<EmbassyResponseDTO> getVisaOrderByOrderId(@Param("orderId") int orderId);
	
	@Modifying
	@Transactional
	@Query("UPDATE EmbassyLetter e SET e.status = :status WHERE e.id = :id")
	void cancelOrder(@Param("id") int id, @Param("status") String status);

	@Query("SELECT COALESCE(MAX(e.syskey), 'ER00000000') FROM EmbassyLetter e")
    String findMaxSysKey();
}

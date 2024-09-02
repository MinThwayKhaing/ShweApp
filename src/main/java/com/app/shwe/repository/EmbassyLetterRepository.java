package com.app.shwe.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.app.shwe.dto.EmbassyLetterDTO;
import com.app.shwe.dto.EmbassyLetterProjectionDTO;
import com.app.shwe.dto.EmbassyLetterResponseDTO;
import com.app.shwe.dto.EmbassyResponseDTO;
import com.app.shwe.dto.VisaExtensionDTO;
import com.app.shwe.dto.VisaExtensionOrderResponseDTO;
import com.app.shwe.model.EmbassyLetter;

import jakarta.transaction.Transactional;

@Repository
public interface EmbassyLetterRepository extends JpaRepository<EmbassyLetter, Integer>{
	
//	@Query("SELECT new com.app.shwe.dto.EmbassyLetterProjectionDTO(v.id,v.syskey,v.passportBio, v.visaPage, v.contactNumber,v.user.userName,v.status) FROM EmbassyLetter v WHERE v.user.id = :id ORDER BY v.id DESC")
//	List<EmbassyLetterProjectionDTO> getEmbassyLetterByUserId(int id);
	
//	@Query("SELECT new com.app.shwe.dto.EmbassyResponseDTO(vo.order_id,vs.serviceName, vo.main_visa_id, vo.sub_visa_id, vt.description, svt.price) "
//			+ "FROM EmbassyVisaType vt "
//			+ "JOIN VisaServices vs ON vt.visa.id = vs.id "
//			+ "JOIN EmbassyLetterOrder vo ON vo.main_visa_id = vs.id "
//			+ "JOIN EmbassySubVisaType svt ON svt.id = vo.sub_visa_id "
//			+ "WHERE vo.order_id = :orderId")
//	List<EmbassyResponseDTO> getVisaOrderByOrderId(@Param("orderId") int orderId);

	@Modifying
	@Transactional
	@Query("UPDATE EmbassyLetter e SET e.status = :status WHERE e.id = :id")
	void changeOrderStatus(@Param("id") int id, @Param("status") String status);

	@Query("SELECT COALESCE(MAX(e.syskey), 'ER00000000') FROM EmbassyLetter e")
    String findMaxSysKey();
	
//	@Query("SELECT new com.app.shwe.dto.EmbassyLetterDTO(v.visaType,v.passportBio, v.visaPage,v.address, v.contactNumber) FROM EmbassyLetter v WHERE v.user.id = :userId")
//	List<EmbassyLetterDTO> getEmbassyLetterByUserId(int userId);
	
	@Query("SELECT new com.app.shwe.dto.EmbassyLetterDTO(v.visaTypeDescription,v.passportBio, v.visaPage, v.contactNumber,v.user.userName,v.status,v.createdDate) "
			+ "FROM EmbassyLetter v JOIN v.user u " + "WHERE v.status = :status "
			+ "AND (:searchString IS NULL OR :searchString = '' OR u.userName LIKE %:searchString%) "
			+ "ORDER BY v.createdDate")
	Page<EmbassyLetterDTO> getAllVisa(@Param("status") String status, @Param("searchString") String searchString,
			Pageable pageable);
	
	@Query("SELECT new com.app.shwe.dto.EmbassyLetterDTO(v.visaTypeDescription,v.passportBio, v.visaPage, v.contactNumber,v.user.userName,v.status,v.createdDate) "
			+ " FROM EmbassyLetter v WHERE v.id =:id")
	Optional<EmbassyLetterDTO> getVisaOrder(@Param("id") int id);
}

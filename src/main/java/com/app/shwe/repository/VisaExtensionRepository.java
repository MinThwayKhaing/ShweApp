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
import com.app.shwe.dto.Report90DayProjectionDTO;
import com.app.shwe.dto.Report90DayTypeResponseDTO;
import com.app.shwe.dto.Tm30DTOResponseDTO;
import com.app.shwe.dto.VisaExtensionDTO;
import com.app.shwe.dto.VisaExtensionOrderResponseDTO;
import com.app.shwe.dto.VisaExtensionProjectionDTO;
import com.app.shwe.dto.VisaExtensionResponseDTO;
import com.app.shwe.dto.VisaExtensionTypeResponseDTO;
import com.app.shwe.model.TermsAndCondition;
import com.app.shwe.model.VisaExtension;

import jakarta.transaction.Transactional;

@Repository
public interface VisaExtensionRepository extends JpaRepository<VisaExtension, Integer> {

	// @Query("SELECT new
	// com.app.shwe.dto.VisaExtensionTypeResponseDTO(v.id,v.description,s.price) "
	// + "FROM Report90DayVisaType v JOIN Report90DaySubVisaType s ON s.visa.id =
	// v.id")
	// List<VisaExtensionTypeResponseDTO> findAllVisaType();

	@Query("SELECT new com.app.shwe.dto.VisaExtensionProjectionDTO(v.id,v.syskey,v.passportBio, v.visaPage, v.contactNumber,v.user.userName,v.status) FROM VisaExtension v WHERE v.user.id = :id ORDER BY v.id DESC")
	List<VisaExtensionProjectionDTO> getVisaExtensionOrderByUserId(int id);

//	@Query("SELECT new com.app.shwe.dto.VisaExtensionOrderResponseDTO(vo.order_id,vs.serviceName, vo.main_visa_id, vo.sub_visa_id, vt.description, svt.price) "
//			+ "FROM VisaExtensionType vt "
//			+ "JOIN VisaServices vs ON vt.visa.id = vs.id "
//			+ "JOIN VisaExtensionOrder vo ON vo.main_visa_id = vs.id "
//			+ "JOIN VisaExtensionSubType svt ON svt.id = vo.sub_visa_id "
//			+ "WHERE vo.order_id = :orderId")
//	List<VisaExtensionOrderResponseDTO> getVisaOrderByOrderId(@Param("orderId") int orderId);

	@Modifying
	@Transactional
	@Query("UPDATE VisaExtension v SET v.status = :status WHERE v.id = :id")
	void changeOrderStatus(@Param("id") int id, @Param("status") String status);

	@Query("SELECT COALESCE(MAX(r.syskey), 'VE00000000') FROM VisaExtension r")
	String findMaxSysKey();

//	@Query("SELECT new com.app.shwe.dto.VisaExtensionDTO(v.visaType,v.passportBio, v.visaPage, v.contactNumber,v.user.userName,v.createdDate) FROM VisaExtension v WHERE v.user.id = :userId")
//	List<VisaExtensionDTO> getVisaExtensionOrder(int userId);
//	
	@Query("SELECT new com.app.shwe.dto.VisaExtensionDTO(v.visaTypeDescription,v.passportBio, v.visaPage, v.contactNumber,v.user.userName,v.status,v.createdDate) "
			+ "FROM VisaExtension v JOIN v.user u " + "WHERE v.status = :status "
			+ "AND (:searchString IS NULL OR :searchString = '' OR u.userName LIKE %:searchString%) "
			+ "ORDER BY v.createdDate")
	Page<VisaExtensionDTO> getAllVisa(@Param("status") String status, @Param("searchString") String searchString,
			Pageable pageable);
	
	@Query("SELECT new com.app.shwe.dto.VisaExtensionDTO(v.visaTypeDescription,v.passportBio, v.visaPage, v.contactNumber,v.user.userName,v.status,v.createdDate) "
			+ " FROM VisaExtension v WHERE v.id =:id")
	Optional<VisaExtensionDTO> getVisaOrderById(@Param("id") int id);

}

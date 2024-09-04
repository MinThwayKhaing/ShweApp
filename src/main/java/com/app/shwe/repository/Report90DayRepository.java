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

import com.app.shwe.dto.Report90DayDTO;
import com.app.shwe.dto.Report90DayProjectionDTO;
import com.app.shwe.dto.Report90DayTypeResponseDTO;
import com.app.shwe.dto.Tm30ProjectionDTO;
import com.app.shwe.dto.VisaExtensionDTO;
import com.app.shwe.dto.VisaResponseDTO;
import com.app.shwe.model.Report90Day;

import jakarta.transaction.Transactional;

@Repository
public interface Report90DayRepository extends JpaRepository<Report90Day, Integer> {

	@Query("SELECT new com.app.shwe.dto.Report90DayProjectionDTO(r.id,r.syskey,r.tm6Photo,r.expireDatePhoto,r.passportBio, r.visaPage, r.contactNumber,r.user.userName,r.status) FROM Report90Day r WHERE r.user.id = :id ORDER BY r.id DESC")
	List<Report90DayProjectionDTO> getReport90DayOrderByUserId(int id);

	// @Query("SELECT new
	// com.app.shwe.dto.Report90DayTypeResponseDTO(vo.order_id,vs.serviceName,
	// vo.main_visa_id, vo.sub_visa_id, vt.description, svt.price) "
	// + "FROM Report90DayVisaType vt "
	// + "JOIN VisaServices vs ON vt.visa.id = vs.id "
	// + "JOIN Report90DayOrder vo ON vo.main_visa_id = vs.id "
	// + "JOIN Report90DaySubVisaType svt ON svt.id = vo.sub_visa_id "
	// + "WHERE vo.order_id = :orderId")
	// List<Report90DayTypeResponseDTO> getVisaOrderByOrderId(@Param("orderId") int
	// orderId);

	@Query("SELECT COUNT(r) FROM Report90Day r WHERE r.id = :id")
	int checkReport90DayById(@Param("id") int id);

	@Modifying
	@Transactional
	@Query("UPDATE Report90Day r SET r.status = :status WHERE r.id = :id")
	void changeOrderStatus(@Param("id") int id, @Param("status") String status);

	@Query("SELECT COALESCE(MAX(r.syskey), 'RP00000000') FROM Report90Day r")
	String findMaxSysKey();

	@Query("SELECT new com.app.shwe.dto.Report90DayDTO(v.syskey,v.visaTypeDescription,v.passportBio, v.visaPage, v.contactNumber,v.user.userName,v.status,v.createdDate) "
			+ "FROM Report90Day v JOIN v.user u " + "WHERE v.status = :status "
			+ "AND (:searchString IS NULL OR :searchString = '' OR u.userName LIKE %:searchString%) "
			+ "ORDER BY v.createdDate")
	Page<Report90DayDTO> getAllVisa(@Param("status") String status, @Param("searchString") String searchString,
			Pageable pageable);
	
	@Query("SELECT new com.app.shwe.dto.Report90DayDTO(v.syskey,v.visaTypeDescription,v.passportBio, v.visaPage, v.contactNumber,v.user.userName,v.status,v.createdDate) "
			+ " FROM Report90Day v WHERE v.id =:id")
	Optional<Report90DayDTO> getVisaOrderById(@Param("id") int id);
}

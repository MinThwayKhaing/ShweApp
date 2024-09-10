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

import com.app.shwe.dto.Tm30DTO;
import com.app.shwe.dto.Tm30DTOResponseDTO;
import com.app.shwe.dto.Tm30DetailsDTO;
import com.app.shwe.dto.Tm30ProjectionDTO;
import com.app.shwe.dto.Tm30ResponseDTO;
import com.app.shwe.dto.VisaResponseDTO;
import com.app.shwe.model.Tm30;

import jakarta.transaction.Transactional;

@Repository
public interface Tm30Repository extends JpaRepository<Tm30, Integer> {

	// @Query("SELECT new com.app.shwe.dto.Tm30ResponseDTO(vo.order_id, t.period,
	// t.passportBio, t.visaPage, t.contactNumber, s.price, s.duration, vt.visaType)
	// " +
	// "FROM Tm30 t " +
	// "JOIN User u ON u.id = t.user.id " +
	// "JOIN VisaType vt ON vt.id = t.visa.id " +
	// "JOIN VisaOrder vo ON vo.main_visa_id = vt.id " +
	// "JOIN SubVisaType s ON s.id = vo.sub_visa_id " +
	// " WHERE vo.main_visa_id = s.visa.id ORDER BY t.createdDate DESC")
	// Page<Tm30ResponseDTO> getAllTm30(Pageable pageable);

	// @Query("SELECT new com.app.shwe.dto.Tm30ResponseDTO(vo.order_id,
	// vo.main_visa_id, vo.sub_visa_id, t.user_id )FROM tm30 t"
	// + " JOIN visa_order vo ON t.id = vo.order_id ")
	// Page<Tm30ResponseDTO> getTM30(Pageable pageable);

	@Query("SELECT v.main_visa_id, v.sub_visa_id FROM VisaOrder v WHERE v.order_id = :id ")
	VisaResponseDTO getTM30(@Param("id") int id);

	@Query("SELECT new com.app.shwe.dto.Tm30DTOResponseDTO(t.id, t.createdDate, t.syskey, t.passportBio, t.visaPage, t.duration, t.contactNumber, t.status, u.userName) "
			+ "FROM Tm30 t JOIN t.user u WHERE t.status = :status AND u.userName LIKE %:searchString% ORDER BY t.createdDate")
	Page<Tm30DTOResponseDTO> getAllTm30(@Param("status") String status, @Param("searchString") String searchString,
			Pageable pageable);

	@Query("SELECT COUNT(t) FROM Tm30 t WHERE t.id = :id")
	int checkTm30ById(@Param("id") int id);

	// @Query("SELECT t.period,t.period,t.passportBio,t.visaPage,t.contactNumber
	// FROM Tm30 t WHERE t.user.id = :id")
	// List<Tm30> getTm30OrderByUserId(int id);

	// @Query("SELECT new com.app.shwe.dto.Tm30ProjectionDTO(t.id,t.syskey,t.period,
	// t.passportBio, t.visaPage, t.contactNumber,t.user.userName,t.status) FROM
	// Tm30 t WHERE t.user.id = :id ORDER BY t.id DESC")
	// List<Tm30ProjectionDTO> getTm30OrderByUserId(int id);

	@Query("SELECT new com.app.shwe.dto.VisaResponseDTO(vo.order_id,vs.serviceName, vo.main_visa_id, vo.sub_visa_id, vt.description, svt.price, svt.duration) "
			+ "FROM VisaType vt " + "JOIN VisaServices vs ON vt.visa.id = vs.id "
			+ "JOIN VisaOrder vo ON vo.main_visa_id = vs.id " + "JOIN SubVisaType svt ON svt.id = vo.sub_visa_id "
			+ "WHERE vo.order_id = :orderId")
	List<VisaResponseDTO> getVisaOrderByOrderId(@Param("orderId") int orderId);

	@Query("SELECT new com.app.shwe.dto.Tm30DetailsDTO("
			+ "t.id, t.syskey, t.passportBio, t.visaPage, t.duration, t.contactNumber, t.status, "
			+ "t.createdDate, u.userName, p.description, p.type, p.price, pr.description) " + "FROM Tm30 t "
			+ "JOIN t.user u " + "JOIN t.price p " + "JOIN t.period pr " + "WHERE t.id = :id")
	Tm30DetailsDTO findTm30DetailsById(@Param("id") int id);
	// @Query("SELECT new com.app.shwe.dto.Tm30ProjectionDTO(t.id,t.syskey,t.period,
	// t.passportBio, t.visaPage, t.contactNumber,t.user.userName,t.status) FROM
	// Tm30 t ORDER BY t.id DESC")
	// List<Tm30ProjectionDTO> getAllTm30Order();

	@Modifying
	@Transactional
	@Query("UPDATE Tm30 t SET t.status = :status WHERE t.id = :id")
	void cancelOrder(@Param("id") int id, @Param("status") String status);
	
	
	@Modifying
	@Transactional
	@Query("UPDATE Tm30 t SET t.status = :status WHERE t.id = :id")
	void changeOrderStatus(@Param("id") int id, @Param("status") String status);

	// @Query("SELECT new com.app.shwe.dto.Tm30DTO(t.period,t.passportBio,
	// t.visaPage, t.contactNumber) FROM Tm30 t WHERE t.user.id = :userId")
	// List<Tm30DTO> getTm30OrderByUserId(int userId);

	@Query("SELECT new com.app.shwe.dto.Tm30ResponseDTO(" + "t.id,t.syskey, "
			+ "t.period.description || ' (฿' || t.period.price || ')', " + "t.passportBio, " + "t.visaPage, "
			+ "t.user.userName, " + "t.contactNumber) " + "FROM Tm30 t " + "WHERE t.syskey = :syskey")
	Tm30ResponseDTO findTm30ResponseBySyskey(@Param("syskey") String syskey);
	
	@Query("SELECT t FROM Tm30 t WHERE t.syskey = :syskey")
	Optional<Tm30> getTm30BySyskey(@Param("syskey")String syskey);

}

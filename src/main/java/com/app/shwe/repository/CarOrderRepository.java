package com.app.shwe.repository;

import com.app.shwe.dto.CarOrderResponseDTO;
import com.app.shwe.dto.TranslatorOrderResponseDTO;
import com.app.shwe.model.CarOrder;

import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CarOrderRepository extends JpaRepository<CarOrder, Integer> {

    // @Query("SELECT new com.app.shwe.dto.CarOrderResponseDTO(" +
    // "u.id, u.userName, co.fromLocation, co.toLocation, " +
    // "co.pickUpDate, co.pickUpTime, co.fromDate, co.toDate, " +
    // "co.carType, co.driver, co.orderConfirm, co.customerPhoneNumber, " +
    // "co.carBrand, co.carId) " +
    // "FROM CarOrder co " +
    // "JOIN User u ON co.createdBy = u.id " +
    // "WHERE u.userName LIKE %:searchString% OR co.fromLocation LIKE
    // %:searchString%")
    // Page<CarOrderResponseDTO> findAllWithSearch(@Param("searchString") String
    // searchString, Pageable pageable);

    @Query("SELECT new com.app.shwe.dto.CarOrderResponseDTO(o.sysKey,c.id,o.createdDate,o.carType,o.carBrand,c.driverName,c.image,o.status)"
            + " FROM CarRent c JOIN CarOrder o ON c.id = o.carId.id WHERE"
            + " (LOWER(o.carBrand) LIKE LOWER(CONCAT('%', :searchString, '%'))"
            + " OR LOWER(c.driverName) LIKE LOWER(CONCAT('%', :searchString, '%')))")
    Page<CarOrderResponseDTO> showCarOrder(@Param("searchString") String searchString, Pageable pageable);

    @Query("SELECT new com.app.shwe.dto.CarOrderResponseDTO(o.sysKey,c.id,o.createdDate,o.carType,o.carBrand,c.driverName,c.image,o.status)"
            + " FROM CarRent c JOIN CarOrder o ON c.id = o.carId.id WHERE"
            + " (LOWER(o.carBrand) LIKE LOWER(CONCAT('%', :searchString, '%'))"
            + " OR LOWER(c.driverName) LIKE LOWER(CONCAT('%', :searchString, '%')))"
            + " AND o.createdBy = :userId")
    Page<CarOrderResponseDTO> findOrderByUserId(@Param("userId") int userId, @Param("searchString") String searchString,
            Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE CarOrder c SET c.status = :status WHERE c.id = :id")
    void updateOrder(@Param("id") int id, @Param("status") String status);

    CarOrder find(Class<CarOrder> class1, int orderId, LockModeType pessimisticWrite);

    @Query("SELECT COALESCE(MAX(t.sysKey), 'CR00000000') FROM CarOrder t")
    String findMaxSysKey();
}
